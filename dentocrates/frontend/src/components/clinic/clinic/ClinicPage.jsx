import "./ClinicPage.css";
import { useEffect, useState } from "react";
import { Loading } from "../../lodaingPage/Loading";
import { role , userId} from "../../../utils/token/TokenDecoder";
import { MultiFetch } from "../../../utils/fetch/MultiFetch";
import { useParams, useNavigate } from "react-router-dom";
import {ReviewPage} from "../../review/Reviews";

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
    const [rating, setRating] = useState(0.0);
    const getClinicData = async () => {
        const clinicDataUrl = `/clinic/${id}`;
        const dentistDataUrl = `/dentist/`;
        const responseData = await data(clinicDataUrl);
        setClinicData(await responseData);
        const dentistResponse = await data(dentistDataUrl + responseData.dentistId);
        setDentistData(dentistResponse);
        const rating = await getRatingOfClinic();
        setRating(await rating);
    };

    const updateAppointment = async (currentId, appearance) => {
        const calendarDataUrl = '/calendar/';
        const requestBody = {id:currentId, clinicId: id, dentistId: userId(), appeared:!appearance};
        await data(calendarDataUrl,'PUT', requestBody);
    }
    const getCalendarData = async () => {
        const clinicCalendarDataUrl = `/calendar/clinic/${id}`;
        const calendarData = await data(clinicCalendarDataUrl);
        const appointmentsData = await calendarData.filter((appointment) =>
            appointment.reservation >= currentDate).sort(
            (a, b) => Number(a.reservation.substring(0,19)
                    .replace(/[T:-]/g, ""))
                -
                Number(b.reservation.substring(0,19)
                    .replace(/[T:-]/g, "")));

        setAppointments(await appointmentsData);
    };

    const getRatingOfClinic = async() => {
        const ratingUrl = `/review/rating/clinic/${id}`;
        return await data(ratingUrl);
    }
    const getCustomerDetails = async() => {
        const customersDataUrl = `/client/all`;
        const responseData = await data(customersDataUrl);
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
        const customerAppointmentsDataUrl = `/calendar/customer/${userId()}`;
        const appointmentsData = await data(customerAppointmentsDataUrl);
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
                        <h1 className="selected-name">
                            {clinicData.name}
                        </h1>
                        <h2>
                            Location: {clinicData.zipCode} {clinicData.city} {clinicData.street}
                        </h2>
                        <h3>{clinicData.clinicType.replaceAll("_"," ")}</h3>
                        <h3>Open: {clinicData.openingHours}</h3>
                        <h3>Dentist: Dr. {dentistData.firstName} {dentistData.lastName}</h3>
                        { rating > 0 ?
                            <h4 className="clinic-rating">
                                Rating: {rating}
                            </h4>
                            :
                            <></>
                        }
                    </div>
                ) : (
                    <Loading />
                )}
                {role() === "DENTIST" && clinicData.dentistId === userId() ? (
                    appointments.map((appointment, index) => (
                        <>
                            <div key={index}
                                 className="listName listMargin customer-box"
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
                <div>
                    <ReviewPage
                        id={id}
                        byClinic={true}
                    />
                </div>
            </div>
        );
    } else {
        return <Loading />;

    }
}

