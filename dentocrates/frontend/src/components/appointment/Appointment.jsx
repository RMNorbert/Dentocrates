import "./Appointment.css";
import {useEffect, useState} from "react";
import {Loading} from "../lodaingPage/Loading";
import {MultiFetch} from "../../utils/fetch/MultiFetch";
import {userId} from "../../utils/token/TokenDecoder";
import {ReviewRegister} from "../review/ReviewRegister";
import {role} from "../../utils/token/TokenDecoder";
import AppointmentCard from "../clinic/calendar/AppointmentCard";
import {dateFormatter} from "../../utils/formatter/DateFormatter";
import {useNavigate} from "react-router-dom";
export const Appointment = () => {
    const { data } = MultiFetch();
    const navigate = useNavigate();
    const currentDate = new Date().toJSON();
    const [isLoaded, setIsLoaded] = useState(false);
    const [appointments, setAppointments] = useState([]);
    const [clinics, setClinics] = useState([]);
    const [customers, setCustomers] = useState([]);
    const getData = async () => {
            const appointmentsDataUrl = `/calendar/customer/${userId()}`;
            const clinicDataUrl = "/clinic/all";
            const appointmentsData = await data(appointmentsDataUrl);
            const clinicsData = await data(clinicDataUrl);

            const appointmentsClinicIds = await appointmentsData.map((element) => element.clinicId)
            const filteredClinicsData = await clinicsData.filter((clinic) => appointmentsClinicIds.includes(clinic.id));
            const sortedAppointments = await appointmentsData.sort(
                (a, b) => Number(a.reservation.substring(0,19)
                        .replace(/[T:-]/g, ""))
                    -
                    Number(b.reservation.substring(0,19)
                        .replace(/[T:-]/g, "")));

            setAppointments(sortedAppointments);
            setClinics(filteredClinicsData);
            setIsLoaded(true);
    }
    const getCustomerDetails = async() => {
        const customersDataUrl = `/client/all`;
        const responseData = await data(customersDataUrl);
        setCustomers(await responseData);
    };
    async function getClinicsAppointments() {
        const clinicListProviderUrl = `/clinic/dentist/${userId()}`;
        const clinicsData = await data(clinicListProviderUrl);
        const clinicIds = await clinicsData.map(appointment => appointment.id);
        let currentAppointments = [];

        for(let i = 0; i <clinicIds.length; i++) {
            const clinicAppointmentsDataUrl = `/calendar/clinic/weekly/${clinicIds[i]}`;
            const clinicAppointmentsData = await data(clinicAppointmentsDataUrl);
            if(clinicAppointmentsData.length > 0) {
                currentAppointments.push(await clinicAppointmentsData);
            }
        }

        if(currentAppointments.length > 0) {
            await getCustomerDetails();
        }
        for(let i = 0; i < currentAppointments.length; i++) {
            currentAppointments[i].sort(
                (a, b) => Number(a.reservation.substring(0,19)
                        .replace(/[T:-]/g, ""))
                    -
                    Number(b.reservation.substring(0,19)
                        .replace(/[T:-]/g, "")));
        }

        setAppointments(currentAppointments);
        setClinics(clinicsData);
        setIsLoaded(true);
    }
    const handleDelete = async (currentId) => {
        const calendarDeleteUrl = "/calendar/";
        const response = await data(calendarDeleteUrl,"DELETE",{userId: userId(), targetId: currentId})
        if(response) {
            setIsLoaded(false);
        }
    };


    useEffect(() => {
        if(!isLoaded){
            if(role() === "CUSTOMER") {
                getData();
            }
            else if (role() === "DENTIST") {
                getClinicsAppointments();
            }
        }
    },[isLoaded])

    if(isLoaded) {
        return (<>
            { role() === "CUSTOMER" &&
            <div className="appointment">
                <h2 className="appointment-text">
                    {appointments.length > 0 ? "Booked appointments:" : "You have no appointments registered yet"}
                </h2>
                <div className="appintment-content">
                {appointments.map((appointment, index) => (
                    <div key={index}
                         className="appointment-box  distanceHolder"
                    >
                        {clinics.map((clinic) => {
                            if (clinic.id === appointment.clinicId) {
                                return <div key={clinic.name} >
                                    <div
                                        className="appointment-clinic-name cursor"
                                        onClick={()=> navigate(`/clinic/${clinic.id}`)}
                                    >
                                        {clinic.name}
                                    </div>
                                    <div className="distanceHolder">Phone: {clinic.contactNumber}</div>
                                    <div className="distanceHolder">Location: {clinic.city} {clinic.street}</div>
                                </div>
                            }
                        })}
                        <div className="appointment-element">
                        <strong>{appointment.reservation.replace("T"," ")}</strong>
                            {appointment.appeared === false  && appointment.reservation < currentDate ?
                                    <label className="missed">
                                        <input type="checkbox"
                                               disabled={true}
                                               className="hidden-checkbox"
                                        /><span className="checkmark">⤫</span>
                                    </label>
                            : appointment.appeared === false ?
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
                            {(!appointment.reviewed && appointment.appeared && new Date(appointment.reservation) < new Date() ) ?
                                <ReviewRegister
                                    clinicId={appointment.clinicId}
                                    appointmentId={appointment.id}
                                />
                                :
                                <>
                                </>
                            }
                        </div>
                    </div>
                ))}
                </div>
            </div>
                }
        {role() === "DENTIST" &&
            <div className="appointment">
                <h2 className="appointment-text">
                    {appointments.length > 0 ? "Booked appointments by clinics:" :
                     "You have no appointments registered to your clinics for the next week"}
                </h2>
                <div className="appintment-content">
                    {appointments.map((appointment, index) => (
                        <div key={index}
                             className="appointment-box  distanceHolder fit"
                        >
                            {clinics.map((clinic) => {
                                if (clinic.id === appointment[index].clinicId) {
                                    return <div
                                                key={clinic.name}
                                                className="appointment-clinic-name cursor"
                                                onClick={()=> navigate(`/clinic/${clinic.id}`)}
                                            >
                                                {clinic.name}
                                            </div>
                                }
                            })}
                            {appointment.map((element) => (
                                <div key={element.reservation}>
                                <AppointmentCard id={element.clinicId}
                                             customers={customers}
                                             appointments={element}
                                             dateFormatter={dateFormatter}/>
                                </div>
                                ))}
                            </div>
                    ))}
                </div>
            </div>}
        </>)
    } else {
        return (<Loading/>)
    }
}
