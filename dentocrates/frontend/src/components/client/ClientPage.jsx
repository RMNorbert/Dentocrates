import "./ClientPage.css";
import React, { useEffect, useState } from "react"
import {role, userId} from "../../utils/token/TokenDecoder";
import {MultiFetch} from "../../utils/fetch/MultiFetch";
import {Loading} from "../lodaingPage/Loading";
import {ReviewPage} from "../review/Reviews";
import {useNavigate} from "react-router-dom";
import ClinicCard from "../clinic/clinic/card/ClinicCard";

export const ClientPage = () => {
    const { data } = MultiFetch();
    const navigate = useNavigate();
    const [clinicData, setClinicData] = useState([]);
    const [clinicId, setClinicId] = useState(0);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [leaveData, setLeaveData] = useState([]);
    const [clientData, setClientData] = useState([]);
    const [isLoaded, setIsLoaded] = useState(false);
    const [isResetRequestMessageHidden, setIsResetRequestMessageHidden] = useState(true);
    const [isDeleteRequested, setIsDeleteRequested] = useState(false);
    const [leaveMessage, setLeaveMessage] = useState("");
    const [isLeaveMessageHidden, setIsLeaveMessageHidden] = useState(true);
    const [validationCode, setValidationCode] = useState("");
    const [validationTextToDelete, setValidationTextToDelete] = useState("");
    const [generatedTextToDelete, setGeneratedTextToDelete] = useState("");
    const [resetCode, setResetCode] = useState("");

    const getClientDetails = async() =>{
        if(role() === "CUSTOMER") {
            const clientDetailsUrl = `/client/${userId()}`;
            const response = await data(clientDetailsUrl);
            setClientData(await response);
        }
        else if(role() === "DENTIST"){
            const clinicDetailsUrl = `/clinic/dentist/${userId()}`;
            const dentistDetailsUrl = `/dentist/${userId()}`;
            const clinicResponse = await data(clinicDetailsUrl);

            setClinicData(await clinicResponse);
            if(clinicResponse[0]) {
                setClinicId(clinicResponse[0].id)
            }

            const response = await data(dentistDetailsUrl);
            setClientData(await response);
            await fetchLeaveData(await clinicResponse);
        }
        setIsLoaded(true);
    }

    const fetchLeaveData = async (clinic) => {
        let leaves = [];
        for (let i = 0; i < clinic.length; i++) {
            const clinicLeaves = await data(`/leave/${clinic[i].id}`);
            leaves.push(clinicLeaves);
        }
        setLeaveData(leaves);
    }

    const handleClinicChange = (event) => {
        let chosen = event.target.value.replace(/\D/g, "");
        setClinicId(chosen);
    };

    const handleStartChange = (event) => {
        let chosen = event.target.value;
        setStartDate(chosen + 'T00:00:00');
    }

    const handleEndChange = (event) => {
        let chosen = event.target.value;
        setEndDate(chosen + 'T00:00:00');
    }

    const handleValidationCodeChange = (event, setState) => {
        setState(event.target.value);
    }

    const handleLeaveSubmit = async () => {
        let registerData = {
            clinicId : clinicId,
            startOfTheLeave : startDate,
            endOfTheLeave : endDate
        }
        try {
            const response = await data("/leave/", "POST", registerData);
            if (response) {
                setIsLoaded(false);
                setStartDate(null);
                setEndDate(null);
            }
        } catch (error) {
            setLeaveMessage("Select dates to register");
            setIsLeaveMessageHidden(false);
            console.error("Error:", error);
        }
    }

    const handleResetRequest = async (email) => {
        const passwordResetRequestUrl = "/verify/reset/register";
        const requestBody = {email: email,  role:role()};
        try {
            await data(passwordResetRequestUrl, "POST", requestBody);
            setIsResetRequestMessageHidden(false);
        } catch (error) {
            console.error("Error:", error);
        }
    };

    const generateRandomString = (length) => {
      const characters =
        'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
      const charactersLength = characters.length;
      let result = '';

      // Create an array of 32-bit unsigned integers
      const randomValues = new Uint32Array(length);

      // Generate random values
      window.crypto.getRandomValues(randomValues);
      randomValues.forEach((value) => {
        result += characters.charAt(value % charactersLength);
      });
      return result;
    }

    const handleDeleteRequest = () => {
        setGeneratedTextToDelete(generateRandomString(10));
        setIsDeleteRequested(true);
    }
    const handleDelete = async (currentId) => {
        if(validationTextToDelete === generatedTextToDelete){
            const clientDeleteUrl = role() === "CUSTOMER" ? "/client/" :
                role() === "DENTIST" ? "/dentist/": false;
            const requestBody = {userId: userId(), targetId: currentId};
            const response =  await data(clientDeleteUrl, "DELETE", requestBody);
            if(response) {
                localStorage.clear();
                const redirectUrl = window.location.toString();
                window.location.replace(redirectUrl.replace('client', ''));
            }
        }
    };

    const handleLeaveDelete = async (currentId, clinic) => {
        const leaveDeleteUrl = "/leave/";
        const requestBody = {dentistId: userId(), leaveId: currentId , clinicId:clinic};
        const response = await data(leaveDeleteUrl,"DELETE",requestBody)
        if(response) {
            setIsLoaded(false);
        }
    };

    const dateFormatter = (date) => {
        return date.substring(0,10);
    }

    useEffect(() => {
            if(!isLoaded){
                getClientDetails();
            }
        },[isLoaded]);

    if(isLoaded) {
        return (
            <div className="lightBackground">
                <div className="client-box">
                    <div className="client-card shadowBorder">
                        <div><strong>Name: {clientData.firstName} {clientData.lastname}</strong></div>
                        <div><strong>Email: {clientData.email}</strong></div>
                    </div>
                {isResetRequestMessageHidden ?
                    <button className="button shadowBorder"
                        onClick={() => handleResetRequest(clientData.email)}
                    >
                        Request reset password link
                    </button>
                    :
                    <div className="inputBox">
                        <input
                            className="shadowLightBorder upperDistanceHolder"
                            type="text"
                            placeholder="provide the verification code to reset your password"
                            onChange={(e) => handleValidationCodeChange(e, setResetCode)}
                        />
                        <button
                            className="shadowBorder"
                            onClick={() => navigate(`/verify/reset/${resetCode}`)}
                        >
                            Send code
                        </button>
                    </div>
                }
                {!isDeleteRequested ?
                    <button className="button shadowBorder"
                        onClick={() => handleDeleteRequest()}
                    >
                            Delete account
                    </button>
                    :
                    <div className="roundBox shadowBorder distanceHolder">
                    The generated text: <strong>{generatedTextToDelete}</strong>
                    <div className="inputBox">
                        <input
                            className="shadowLightBorder upperDistanceHolder"
                            type="text"
                            placeholder="Write here the generated text upper to this to delete your account"
                            onChange={(e) => handleValidationCodeChange(e, setValidationTextToDelete)}
                        />
                        <button
                            className="shadowBorder"
                            onClick={() => handleDelete(clientData.id)}
                        >
                            Confirm
                        </button>
                        <button
                            className="shadowBorder"
                                onClick={() => setIsDeleteRequested(false)}
                        >
                            Cancel
                        </button>
                    </div>
                    </div>
                }
                {role() && clientData.verified === false ?
                <div className="inputBox upperDistanceHolder">
                    <input
                        className="shadowLightBorder"
                        type="text"
                        placeholder="provide code to validate account"
                        onChange={(e) => handleValidationCodeChange(e, setValidationCode)}
                    />
                    <button
                        className="shadowBorder"
                        onClick={() => navigate(`/verify/${validationCode}`)}
                    >
                        Send code
                    </button>
                </div> :
                    <></>
                }
                {clientData && role() === "DENTIST" &&
                <div className="centered">
                    <div>
                        <ClinicCard clinicList={clinicData}/>
                    </div>
                    <strong><p
                        className="inputBox"
                        hidden={isLeaveMessageHidden}>
                        {leaveMessage}
                    </p></strong>
                    <div className="inputBox shadowBorder roundBox">
                        <label htmlFor="clinic">Clinic:</label>
                        <select id="clinic"  onClick={handleClinicChange}>
                            {clinicData.map((clinic) =>
                                <option key={clinic.id} >{clinic.name} - {clinic.id}</option>)}
                        </select>
                        <label htmlFor="clinic">Start of the leave:</label>
                        <input type="date" id="start" onChange={(e) => handleStartChange(e)}/>
                        <label htmlFor="clinic">End of the leave:</label>
                        <input type="date" id="end" onChange={(e) => handleEndChange(e)}/>
                    </div>
                    <button className="button shadowBorder"
                            onClick={() => handleLeaveSubmit()}
                    >
                        Register leave
                    </button>
                    <div className="leave-box">
                    {leaveData.map((innerArray) =>
                        innerArray.map((leave) => (
                                leave.id ?
                                    <div key={leave.id}
                                        className="leave-card shadowBorder roundBox distanceHolder"
                                    >
                                        <div className="leave-start">
                                           <strong>Start of the leave: {dateFormatter(leave.startOfTheLeave)}</strong>
                                        </div>
                                        <div className="leave-end">
                                        <strong>End of the leave: {dateFormatter(leave.endOfTheLeave)}</strong>
                                            <button className="appointment-delete"
                                                    onClick={() => handleLeaveDelete(leave.id,leave.clinicId)}
                                            >
                                                Delete
                                            </button>
                                        </div>
                                    </div>
                                    :
                                    <></>
                        ))
                    )}
                    </div>
                </div>
                }
                {clientData && role() === "CUSTOMER" &&
                    <div className="centered">
                        <ReviewPage
                            id={clientData.id}
                            byClinic={false}
                        />
                    </div>
                }
                </div>
            </div>
        )
    } else {
        return (<Loading/>)
    }
}
