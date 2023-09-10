import "./Appointment.css";
import React, {useEffect, useState} from "react";
import {Loading} from "../elements/Loading";
import {MultiFetch} from "../../fetch/MultiFetch";
import {userId} from "../token/TokenDecoder";

export const Appointment = () => {
    const { data } = MultiFetch();
    const currentDate = new Date().toJSON();
    const [isLoaded, setIsLoaded] = useState<boolean>(false);
    const [appointments, setAppointments] = useState<AppointmentDTO[]>([]);
    const [clinics, setClinics] = useState<ClinicResponseDTO[]>([]);
    const getData = async () => {
        const appointmentsDataUrl = `/calendar/customer/${userId()}`;
        const clinicDataUrl = '/clinic/all';
        const appointmentsData = await data(appointmentsDataUrl);
        const clinicsData = await data(clinicDataUrl);

        const appointmentsClinicIds = await appointmentsData.map((element:AppointmentDTO ) => element.clinicId);
        const filteredClinicsData = await clinicsData.filter((clinic: ClinicResponseDTO) => appointmentsClinicIds.includes(clinic.id));
        const sortedAppointments = await appointmentsData.sort(
            (a: AppointmentDTO, b: AppointmentDTO) =>
                Number(
                    a.reservation.substring(0, 19).replace(/[T:-]/g, '')
                ) -
                Number(
                    b.reservation.substring(0, 19).replace(/[T:-]/g, '')
                )
        );

        setAppointments(sortedAppointments);
        setClinics(filteredClinicsData);
        setIsLoaded(true);
    };


    const handleDelete = async (currentId: number) => {
        const calendarDeleteUrl:string = '/calendar/';
        const response = await data(calendarDeleteUrl,'DELETE',{userId: userId(), targetId: currentId})
        if(response) {
            setIsLoaded(false);
        }
    };

    useEffect(() => {
        if(!isLoaded){
            getData();
        }
    },[isLoaded])

    if(isLoaded) {
        return (
            <div className="appointment">
                <h2>Booked appointments:</h2>
                {appointments.map((appointment, index) => (
                    <div key={index}
                         className="appointment-box"
                    >
                        {clinics.map((clinic) => {
                            if (clinic.id === appointment.clinicId) {
                               return <div className="appointment-clinic-name">{clinic.name}</div>
                            }
                        })}
                        <div className="appointment-element">
                            {appointment.reservation.replace("T"," ")}
                            {!appointment.appeared  && appointment.reservation < currentDate ?
                                    <label className="missed">
                                        <input type="checkbox"
                                               disabled={true}
                                               className="hidden-checkbox"
                                        /><span className="checkmark">⤫</span>
                                    </label>
                            : !appointment.appeared ?
                                    <label className="default">
                                        <input type="checkbox"
                                               disabled={true}
                                               className="hidden-checkbox"
                                        />
                                    </label>
                            :
                                    <label className="appended">
                                        <input type="checkbox"
                                               disabled={true}
                                               className="hidden-checkbox"
                                        /><span className="checkmark">✔</span>
                                        </label>
                            }
                        <button className="appointment-delete"
                                onClick={() => handleDelete(appointment.id)}
                        >
                            Delete
                        </button>
                        </div>
                    </div>
                ))}
            </div>
        )
    } else {
        return (<Loading/>)
    }
}
