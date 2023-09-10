import "./ClientPage.css";
import React, {ChangeEvent, useEffect, useState} from "react"
import {role, userId} from "../token/TokenDecoder";
import {MultiFetch} from "../../fetch/MultiFetch";
import {Loading} from "../elements/Loading";

export const ClientPage: React.FC = () => {
    const { data } = MultiFetch();
    const [clinicData, setClinicData] = useState<ClinicResponseDTO[]>([]);
    const [clinicId, setClinicId] = useState<number>(0);
    const [startDate, setStartDate] = useState<string | null>(null);
    const [endDate, setEndDate] = useState<string | null>(null);
    const [leaveData, setLeaveData] = useState<Leave[]>([]);
    const [clientData, setClientData] = useState<CustomerResponseDTO | DentistResponseDTO | null>(null);
    const [isLoaded, setIsLoaded] = useState<boolean>(false);

    const getClientDetails = async () => {
        if (role() === 'CUSTOMER') {
            const clientDetailsUrl = `/client/${userId()}`;
            const response:CustomerResponseDTO = await data(clientDetailsUrl);
            setClientData(response);
        } else if (role() === 'DENTIST') {
            const clinicDetailsUrl = `/clinic/dentist/${userId()}`;
            const dentistDetailsUrl = `/dentist/${userId()}`;
            const clinicResponse = await data(clinicDetailsUrl);

            setClinicData(clinicResponse);
            setClinicId(clinicResponse[0].id);

            const response:DentistResponseDTO = await data(dentistDetailsUrl);
            setClientData(response);
            await fetchLeaveData(clinicResponse);
        }
    };

    const fetchLeaveData = async (clinic: ClinicResponseDTO[]) => {
        const leaves: Leave[] = [];
        for (let i = 0; i < clinic.length; i++) {
            const clinicLeaves = await data(`/leave/${clinic[i].id}`);
            if (clinicLeaves.status === 200) {
                leaves.push(...clinicLeaves);
            }
        }
        setLeaveData(leaves);
    };

    const handleClinicChange = (event: ChangeEvent<HTMLSelectElement>) => {
        let chosen = event.target.value.replace(/\D/g, '');
        setClinicId(parseInt(chosen));
    };

    const handleStartChange = (event: ChangeEvent<HTMLInputElement>) => {
        let chosen = event.target.value;
        setStartDate(chosen + 'T00:00:00');
    };

    const handleEndChange = (event: ChangeEvent<HTMLInputElement>) => {
        let chosen = event.target.value;
        setEndDate(chosen + 'T00:00:00');
    };

    const handleLeaveSubmit = async () => {
        let registerData = {
            clinicId: clinicId,
            startOfTheLeave: startDate,
            endOfTheLeave: endDate,
        };
        try {
            const response = await data('/leave/', 'POST', registerData);
            if (response) {
                setIsLoaded(false);
            }
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const handleResetRequest = async (email: string | undefined) => {
        const passwordResetRequestUrl = '/verify/reset/register/';
        const requestBody = { email: email, role: role() };
        try {
            await data(passwordResetRequestUrl, 'POST', requestBody);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const handleDelete = async (currentId: number | undefined) => {
        const clientDeleteUrl: string =
            role() === 'CUSTOMER'
                ? '/client/'
                : role() === 'DENTIST'
                    ? '/dentist/'
                    : '';
        const requestBody = { userId: userId(), targetId: currentId };
        const response = await data(clientDeleteUrl, 'DELETE', requestBody);
        if (response) {
            localStorage.clear();
            const loginUrl = 'http://localhost:3000/';
            window.location.replace(loginUrl);
        }
    };

    const handleLeaveDelete = async (currentId: number, clinic: number) => {
        const leaveDeleteUrl = '/leave/';
        const requestBody = { dentistId: userId(), leaveId: currentId, clinicId: clinic };
        const response = await data(leaveDeleteUrl, 'DELETE', requestBody);
        if (response) {
            setIsLoaded(false);
        }
    };

    const dateFormatter = (date: string) => {
        return date.substring(0, 10);
    };

    useEffect(() => {
        if (!isLoaded) {
            getClientDetails();
            setIsLoaded(true);
        }
    }, [isLoaded]);

    if (isLoaded) {
        return (
            <div className="client-box">
                <div className="client-card">
                    <div>
                        Name: {clientData?.firstName} {clientData?.lastName}
                    </div>
                    <div>Email: {clientData?.email}</div>
                </div>
                <button className="button" onClick={() => handleResetRequest(clientData?.email)}>
                    Request reset password link
                </button>
                <button className="button" onClick={() => handleDelete(clientData?.id)}>
                    Delete account
                </button>
                {clientData && role() === 'DENTIST' && (
                    <div>
                        <div className="inputBox">
                            <label htmlFor="clinic">Clinic:</label>
                            <select id="clinic" onChange={handleClinicChange}>
                                {clinicData.map((clinic) => (
                                    <option key={clinic.id}>
                                        {clinic.name} - {clinic.id}
                                    </option>
                                ))}
                            </select>
                            <label htmlFor="clinic">Start of the leave:</label>
                            <input type="date" id="start" onChange={(e) => handleStartChange(e)} />
                            <label htmlFor="clinic">End of the leave:</label>
                            <input type="date" id="end" onChange={(e) => handleEndChange(e)} />
                        </div>
                        <button className="button" onClick={() => handleLeaveSubmit()}>
                            Register leave
                        </button>
                        <div className="leave-box">
                            {leaveData.map((leave: Leave) =>
                                    leave.id ? (
                                        <div key={leave.id} className="leave-card">
                                            <div className="leave-start">
                                                Start of the leave: {dateFormatter(leave.startOfTheLeave)}
                                            </div>
                                            <div className="leave-end">
                                                End of the leave: {dateFormatter(leave.endOfTheLeave)}
                                                <button
                                                    className="appointment-delete"
                                                    onClick={() => handleLeaveDelete(leave.id, leave.clinicId)}
                                                >
                                                    Delete
                                                </button>
                                            </div>
                                        </div>
                                    ) : (
                                        <></>
                                    )
                                )}
                        </div>
                    </div>
                )}
            </div>
        )
    } else {
        return (<Loading/>)
    }
}
