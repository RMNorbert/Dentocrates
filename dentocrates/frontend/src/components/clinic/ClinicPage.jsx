import { useEffect, useState } from "react";
import { Loading } from "../elements/Loading";
import { role , userId} from "../token/TokenDecoder";
import { MultiFetch } from "../../fetch/MultiFetch";
import { useParams, useNavigate } from "react-router-dom";
export const ClinicPage = () => {
    const { id } = useParams();
    const { data } = MultiFetch();
    const navigate = useNavigate();
    const [isLoaded, setDataLoaded] = useState(false);
    const [clinicData, setClinicData] = useState([]);
    const [appointments, setAppointments] = useState([]);
    const [customerAppointments, setCustomerAppointments] = useState([]);
    const [customers, setCustomers] = useState([]);
    const getClinicData = async () => {
        const responseData = await data(`/clinic/${id}`);
        setClinicData(await responseData);
        setDataLoaded(true);
    };

    const getCalendarData = async () => {
        const calendarData = await data(`/calendar/clinic/${id}`);
        setAppointments(await calendarData);
    };
    const getCustomerDetails = async() =>{
        const responseData = await data(`/client/all`);
        setCustomers(await responseData);
    }
    const filterCustomerDetails = (customerId) => {
       return customers.filter((customer) => customer.id === customerId)
           .map((customer,index) => (
            <div key={index}>
                <h2 className="listName listMargin"> {customer.firstName} {customer.lastName}</h2>
            </div>
           ))
    }
    const getCustomerAppointments = async () => {
        const appointmentsData = await data(`/calendar/customer/${userId()}`);
        setCustomerAppointments(await appointmentsData);
    };

    useEffect(() => {
        if (!isLoaded) {
            if (role() === "DENTIST") {
                getCustomerDetails();
                getCalendarData();
            } else {
                getCustomerAppointments();
            }
            getClinicData();
        }
    }, [id, role, isLoaded]);

    if (isLoaded) {
        return (
            <div className="clinic">
                {clinicData.length > 0 ? (
                    clinicData.map((clinic) => (
                        <div key={clinic.name}>
                            <h1>{clinic.name}</h1>
                            <h2>
                                Location :{clinic.zipCode} {clinic.city} {clinic.street}
                            </h2>
                            <h3>{clinic.clinicType}</h3>
                            <h3>Open: {clinic.openingHours}</h3>
                        </div>
                    ))
                ) : (
                    <Loading />
                )}
                {role === "DENTIST" ? (
                    appointments.map((appointment) => (
                    <>
                        <div key={appointment.id}>{appointment.reservation}</div>
                        {filterCustomerDetails(appointment.customerId)}
                    </>
                        ))
                    ) :
                    <div>
                    (customerAppointments.map((appointment) => (
                        <div>
                            <div key={appointment.reservation}>{appointment.reservation}</div>
                        </div>
                    )))
                        {/*{*TODO:prop opening hour/ booked appointments/store date type *}*/}
                        <button onClick={() => navigate("/calendar/" + id)}>Book an appointment</button>
                    </div>
                }
            </div>
        );
    } else {
        return <Loading />;
    }
};
