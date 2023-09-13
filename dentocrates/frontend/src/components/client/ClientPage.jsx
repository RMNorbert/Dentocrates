import "./ClientPage.css";
import React, { useEffect, useState } from "react"
import {role, userId} from "../token/TokenDecoder";
import {MultiFetch} from "../../fetch/MultiFetch";
import {Loading} from "../elements/Loading";

export const ClientPage = () => {
    const { data } = MultiFetch();
    const [clinicData, setClinicData] = useState([]);
    const [clinicId, setClinicId] = useState(0);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [leaveData, setLeaveData] = useState([]);
    const [clientData, setClientData] = useState([]);
    const [isLoaded, setIsLoaded] = useState(false);
    const getClientDetails = async() =>{
        if(role() === "CUSTOMER") {
            const clientDetailsUrl = `/java-backend/client/${userId()}`;
            const response = await data(clientDetailsUrl);
            setClientData(await response);
        }
        else if(role() === "DENTIST"){
            const clinicDetailsUrl = `/java-backend/clinic/dentist/${userId()}`;
            const dentistDetailsUrl = `/java-backend/dentist/${userId()}`;
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
            const clinicLeaves = await data(`/java-backend/leave/${clinic[i].id}`);
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
    const handleLeaveSubmit = async () => {
        let registerData = {
            clinicId : clinicId,
            startOfTheLeave : startDate,
            endOfTheLeave : endDate
        }
        try {
            const response = await data('/java-backend/leave/', "POST", registerData);
            if (response) {
                setIsLoaded(false);
            }
        } catch (error) {
            console.error('Error:', error);
        }
    }
    const handleResetRequest = async (email) => {
        const passwordResetRequestUrl = '/java-backend/verify/reset/register/';
        const requestBody = {email: email,  role:role()};
        try {
            await data(passwordResetRequestUrl, 'POST', requestBody);
        } catch (error) {
            console.error('Error:', error);
        }
    };
    const handleDelete = async (currentId) => {
        const clientDeleteUrl = role() === "CUSTOMER" ? '/java-backend/client/' : role() === "DENTIST" ? '/java-backend/dentist/': false;
        const requestBody = {userId: userId(), targetId: currentId};
        const response =  await data(clientDeleteUrl, 'DELETE', requestBody);
        if(response) {
            localStorage.clear();
            const loginUrl = 'http://localhost:3000/';
            window.location.replace(loginUrl);
        }
    };

    const handleLeaveDelete = async (currentId, clinic) => {
        const leaveDeleteUrl = '/java-backend/leave/';
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
                        <div>Name: {clientData.firstName} {clientData.lastname}</div>
                        <div>Email: {clientData.email}</div>
                    </div>
                    <button className="button"
                        onClick={() => handleResetRequest(clientData.email)}
                    >
                        Request reset password link
                    </button>
                    <button className="button"
                        onClick={() => handleDelete(clientData.id)}
                    >
                        Delete account
                    </button>
                {clientData && role() === "DENTIST" &&
                <div>
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
            </div>
        )
    } else {
        return (<Loading/>)
    }
}
