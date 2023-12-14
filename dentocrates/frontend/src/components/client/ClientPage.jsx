import "./ClientPage.css";
import React, { useEffect, useState } from "react"
import {role, userId} from "../../utils/token/TokenDecoder";
import {MultiFetch} from "../../utils/fetch/MultiFetch";
import {Loading} from "../lodaingPage/Loading";
import {ReviewPage} from "../review/Reviews";
import {useNavigate} from "react-router-dom";

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
    const [leaveMessage, setLeaveMessage] = useState('');
    const [isLeaveMessageHidden, setIsLeaveMessageHidden] = useState(true);
    const [validationCode, setValidationCode] = useState('');
    const [resetCode, setResetCode] = useState('');
    const getClientDetails = async() =>{
        if(role() === "CUSTOMER") {
            const clientDetailsUrl = `/dentocrates/client/${userId()}`;
            const response = await data(clientDetailsUrl);
            setClientData(await response);
        }
        else if(role() === "DENTIST"){
            const clinicDetailsUrl = `/dentocrates/clinic/dentist/${userId()}`;
            const dentistDetailsUrl = `/dentocrates/dentist/${userId()}`;
            const clinicResponse = await data(clinicDetailsUrl);

            setClinicData(await clinicResponse);
            setClinicId(clinicResponse[0].id)

            const response = await data(dentistDetailsUrl);
            setClientData(await response);
            await fetchLeaveData(await clinicResponse);
        }
    }

    const fetchLeaveData = async (clinic) => {
        let leaves = [];
        for (let i = 0; i < clinic.length; i++) {
            const clinicLeaves = await data(`/dentocrates/leave/${clinic[i].id}`);
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
            const response = await data('/dentocrates/leave/', "POST", registerData);
            if (response) {
                setIsLoaded(false);
            }
        } catch (error) {
            setLeaveMessage("Select dates to register");
            setIsLeaveMessageHidden(false);
            console.error('Error:', error);
        }
    }
    const handleResetRequest = async (email) => {
        const passwordResetRequestUrl = '/dentocrates/verify/reset/register';
        const requestBody = {email: email,  role:role()};
        try {
            await data(passwordResetRequestUrl, 'POST', requestBody);
            setIsResetRequestMessageHidden(false);
        } catch (error) {
            console.error('Error:', error);
        }
    };
    const handleDelete = async (currentId) => {
        const clientDeleteUrl = role() === "CUSTOMER" ? '/dentocrates/client/' :
            role() === "DENTIST" ? '/dentocrates/dentist/': false;
        const requestBody = {userId: userId(), targetId: currentId};
        const response =  await data(clientDeleteUrl, 'DELETE', requestBody);
        if(response) {
            localStorage.clear();
            const redirectUrl = window.location.toString();
            window.location.replace(redirectUrl.replace('client', ''));
        }
    };

    const handleLeaveDelete = async (currentId, clinic) => {
        const leaveDeleteUrl = '/dentocrates/leave/';
        const requestBody = {dentistId: userId(), leaveId: currentId , clinicId:clinic};
        const response = await data(leaveDeleteUrl,'DELETE',requestBody)
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
                setIsLoaded(true);
            }
        },[isLoaded]);

    if(isLoaded) {
        return (
            <div className="client-box">
                    <div className="client-card">
                        <div>Name: {clientData.firstName} {clientData.lastName}</div>
                        <div>Email: {clientData.email}</div>
                    </div>
                {isResetRequestMessageHidden ?
                    <button className="button"
                        onClick={() => handleResetRequest(clientData.email)}
                    >
                        Request reset password link
                    </button>
                    :
                    <div className="inputBox">
                        <input
                            type="text"
                            placeholder="provide the verification code to reset your password"
                            onChange={(e) => handleValidationCodeChange(e, setResetCode)}
                        />
                        <button onClick={() => navigate(`/verify/reset/${resetCode}`)}>Send code</button>
                    </div>
                }
                    <button className="button"
                        onClick={() => handleDelete(clientData.id)}
                    >
                        Delete account
                    </button>
                {role() && clientData.verified === false ?
                <div className="inputBox">
                    <input
                        type="text"
                        placeholder="provide code to validate account"
                        onChange={(e) => handleValidationCodeChange(e, setValidationCode)}
                    />
                    <button onClick={() => navigate(`/verify/${validationCode}`)}>Send code</button>
                </div> :
                    <></>
                }
                {clientData && role() === "DENTIST" &&
                <div>
                    <p
                        className="inputBox"
                        hidden={isLeaveMessageHidden}>
                        {leaveMessage}
                    </p>
                    <div className="inputBox">
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
                    <button className="button"
                            onClick={() => handleLeaveSubmit()}
                    >
                        Register leave
                    </button>
                    <div className="leave-box">
                    {leaveData.map((innerArray) =>
                        innerArray.map((leave) => (
                                leave.id ?
                                    <div key={leave.id}
                                        className="leave-card"
                                    >
                                        <div className="leave-start">
                                           Start of the leave: {dateFormatter(leave.startOfTheLeave)}
                                        </div>
                                        <div className="leave-end">
                                           End of the leave: {dateFormatter(leave.endOfTheLeave)}
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
                {clientData && role() === "Customer" &&
                    <div>
                        <ReviewPage
                            id={clientData.id}
                            byClinic={false}
                        />
                    </div>
                }
            </div>
        )
    } else {
        return (<Loading/>)
    }
}
