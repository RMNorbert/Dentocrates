import "./ClinicPage.css";
import { useEffect, useState } from "react";
import { Loading } from "../elements/Loading";
import { role , userId} from "../token/TokenDecoder";
import { MultiFetch } from "../../fetch/MultiFetch";
import { useParams, useNavigate } from "react-router-dom";

export const ClinicPage = () => {
    const { id } = useParams();
    const { data } = MultiFetch();
    const currentDate = new Date().toJSON();
    const navigate = useNavigate();
    const [isLoaded, setDataLoaded] = useState(false);
    const [isFetched, setIsFetched] = useState(false);
    const [clinicData, setClinicData] = useState([]);
    const [dentistData, setDentistData] = useState([]);
    const [appointments, setAppointments] = useState([]);
    const [customerAppointments, setCustomerAppointments] = useState([]);
    const [customers, setCustomers] = useState([]);

    const getClinicData = async () => {
        const responseData = await data(`/clinic/${id}`);
        setClinicData(await responseData);
        const dentistResponse = await data(`/dentist/${responseData.dentistId}`);
        setDentistData(dentistResponse);
    };

    const updateAppointment = async (currentId, appearance) => {
        const requestBody = {id:currentId, clinicId: id, dentistId: userId(), appeared:!appearance};
        await data('/calendar/','PUT', requestBody);
    }
    const getCalendarData = async () => {
        const calendarData = await data(`/calendar/clinic/${id}`);
        const appointmentsData = await calendarData.filter((appointment) =>
            appointment.reservation >= currentDate).sort(
            (a, b) => Number(a.reservation.substring(0,19)
                    .replace(/[T:-]/g, ""))
                -
                Number(b.reservation.substring(0,19)
                    .replace(/[T:-]/g, "")));

        setAppointments(await appointmentsData);
    };

    const getCustomerDetails = async() => {
        const responseData = await data(`/client/all`);
        setCustomers(await responseData);
    };

    const filterCustomerDetails = (customerId) => {
        return customers.filter((customer) => customer.id === customerId)
            .map((customer,index) => (
                <div key={index}>
                    <h2 className="listName listMargin">Name: {customer.firstName} {customer.lastName}</h2>
                </div>
            ))
    };

    const dateFormatter = (date) => {
        return date.substring(0,16).replace("T"," ");
    }
    const getCustomerAppointments = async () => {
        const appointmentsData = await data(`/calendar/customer/${userId()}`);
        const sortedAppointments = await appointmentsData.filter((appointment) =>
            appointment.clinicId === Number(id) && appointment.reservation >= currentDate).sort(
                (a, b) => Number(a.reservation.substring(0,19)
                        .replace(/[T:-]/g, ""))
                    -
                    Number(b.reservation.substring(0,19)
                        .replace(/[T:-]/g, "")));
        setCustomerAppointments(sortedAppointments);
    };

    const fetchData = async () => {
        if (role() === "DENTIST") {
            await getCustomerDetails();
            await getCalendarData();
            await getClinicData();
        } else if(role() === "CUSTOMER"){
            await getCustomerAppointments();
            await getClinicData();
        }
        setIsFetched(true);
    }

    useEffect(() => {
        if (!isFetched && !isLoaded) {
            fetchData()
        }
        else if(isFetched){
            setDataLoaded(true);
        }
    }, [isFetched, isLoaded]);

    if (isLoaded) {
        return (
            <div className="clinic">
                {clinicData ? (
                    <div className="selected-clinic">
                        <h1 className="selected-name">{clinicData.name}</h1>
                        <h2>
                            Location: {clinicData.zipCode} {clinicData.city} {clinicData.street}
                        </h2>
                        <h3>{clinicData.clinicType.replaceAll("_"," ")}</h3>
                        <h3>Open: {clinicData.openingHours}</h3>
                        <h3>Dentist: Dr. {dentistData.firstName} {dentistData.lastName}</h3>
                    </div>
                ) : (
                    <Loading />
                )}
                {role() === "DENTIST" ? (
                    appointments.map((appointment, index) => (
                        <>
                            <div key={index}
                                 className="listName listMargin"
                            >
                                {dateFormatter(appointment.reservation)}
                                <input
                                    className="fulfilled"
                                    type="checkbox"
                                    onChange={() => updateAppointment(appointment.id,appointment.appeared)}
                                />
                            {filterCustomerDetails(appointment.customerId)}
                            </div>
                        </>
                    ))
                ) : (
                    <div>
                        <button className="button" onClick={() => navigate("/calendar/" + id)}>Book an appointment</button>
                        {customerAppointments.map((appointment, index) => (
                            <div key={index}
                            className="customer"
                            >
                                <div className="customer-appointment">
                                    {appointment.reservation.replace("T"," ")}
                                    <input
                                        className="fulfilled"
                                        type="checkbox"
                                        disabled={true}
                                    />
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        );
    } else {
        return <Loading />;
    }
};
