import "./ClinicPage.css";
import { MdCheckBox } from "react-icons/md";
import { useEffect, useState } from "react";
import { Loading } from "../../lodaingPage/Loading";
import { role , userId} from "../../../utils/token/TokenDecoder";
import { MultiFetch } from "../../../utils/fetch/MultiFetch";
import { useParams, useNavigate } from "react-router-dom";
import {ReviewPage} from "../../review/Reviews";
import AppointmentCard from "../calendar/AppointmentCard";
import {dateFormatter} from "../../../utils/formatter/DateFormatter";

export const ClinicPage = () => {
    const { id } = useParams();
    const { data } = MultiFetch();
    const currentDateTime = new Date();
    const month = String(currentDateTime.getMonth() + 1).padStart(2, '0'); // Months are zero-based
    const day = String(currentDateTime.getDate()).padStart(2, '0');
    const currentDate = `${currentDateTime.getFullYear()}-${month}-${day}`;
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
        setClinicData(responseData);
        const dentistResponse = await data(dentistDataUrl + responseData.dentistId);
        setDentistData(dentistResponse);
        const rating = await getRatingOfClinic();
        setRating(await rating);
    };

    const getCalendarData = async () => {
        const nextDay = currentDate.substring(0,currentDate.length - 2) +
            String(currentDateTime.getDate() + 1).padStart(2, '0');
        const clinicCalendarDataUrl = `/calendar/clinic/${id}`;
        const calendarData = await data(clinicCalendarDataUrl);
        const appointmentsData = await calendarData.filter((appointment) =>
            appointment.reservation >= currentDate && appointment.reservation < nextDay).sort(
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
                    <div className="selected-clinic shadowBorder distanceHolder">
                        <h1 className="selected-name shadowLightBorder" >
                            {clinicData.name}
                        </h1>
                        <h2 id="content">
                            Location: {clinicData.zipCode} {clinicData.city} {clinicData.street}
                        </h2>
                        <h3 id="content">{clinicData.clinicType.replaceAll("_"," ")}</h3>
                        <h3 id="content">Open: {clinicData.openingHours}</h3>
                        <h3 id="content">Phone: {clinicData.contactNumber}</h3>
                        <h3 id="content">Dentist: Dr. {dentistData.firstName} {dentistData.lastName}</h3>
                        <div>
                        { rating > 0 ?
                            <h4 className="clinic-rating">
                                Rating: {rating}
                            </h4>
                            :
                            <></>
                        }
                        {role() === "DENTIST" && clinicData.dentistId == userId() &&
                            <button
                                className="button shadowBorder updateButton clinic-updater"
                                onClick={() => navigate(`/clinic/update/${id}`)}
                            >
                                Update clinic
                            </button>
                        }
                        </div>
                    </div>
                ) : (
                    <Loading />
                )}
                {role() === "DENTIST" && clinicData.dentistId == userId() ? (
                    appointments && appointments.map((currentAppointment, index) => (
                        <div key={index}>
                            <AppointmentCard id={id}
                                             customers={customers}
                                             appointments={currentAppointment}
                                             dateFormatter={dateFormatter}/>
                        </div>
                    ))
                ) : (
                    <div>
                        <button
                            className="customer-box button shadowBorder"
                            onClick={() => navigate("/calendar/" + id)}
                        >
                            Book an appointment
                        </button>
                        {customerAppointments.length > 0 && <h3 className="reviews-list">Appointments</h3>}
                        {customerAppointments.map((appointment, index) => (
                            <div key={index}
                            className="customer"
                            >
                                <div
                                    className="listName listMargin customer-box shadowLightBorder"
                                    id={appointment.appeared ? "appeared" : "not-appeared"}
                                >
                                   <strong>{appointment.reservation.replace("T"," ")}</strong>
                                    {appointment.appeared ?
                                        <MdCheckBox/>
                                        :
                                        <input
                                            id="customer-appointment-card"
                                            type="checkbox"
                                            checked={appointment.appeared}
                                            disabled={true}
                                        />
                                    }
                                </div>
                            </div>
                        ))}
                    </div>
                )}
                <div className="distanceHolder">
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

