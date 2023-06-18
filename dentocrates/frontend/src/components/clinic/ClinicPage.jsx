import { useEffect, useState } from "react";
import { Loading } from "../elements/Loading";
import { role , userId} from "../token/TokenDecoder";
import { MultiFetch } from "../../fetch/MultiFetch";
import { useParams } from "react-router-dom";
export const ClinicPage = () => {
    const { id } = useParams();
    const { data } = MultiFetch();
    const [isLoaded, setDataLoaded] = useState(false);
    const [clinicData, setClinicData] = useState([]);
    const [appointments, setAppointments] = useState([]);
    const [customerAppointments, setCustomerAppointments] = useState([]);

    const getClinicData = async () => {
        console.log(id)
        const responseData = await data(`/clinic/${id}`);
        setClinicData(await responseData);
        setDataLoaded(true);
    };

    const getCalendarData = async () => {
        const calendarData = await data(`/calendar/clinic/${id}`);
        setAppointments(await calendarData);
    };

    const getCustomerAppointments = async () => {
        console.log(userId())
        const appointmentsData = await data(`/calendar/customer/${userId()}`);
        setCustomerAppointments(await appointmentsData);
    };

    useEffect(() => {
        if (!isLoaded) {
            if (role() === "DENTIST") {
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
            </div>
        );
    } else {
        return <Loading />;
    }
};
