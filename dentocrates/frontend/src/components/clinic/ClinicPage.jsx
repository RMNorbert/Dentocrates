import { useEffect, useState } from "react";
import { Loading } from "../elements/Loading";
export const ClinicPage = (clinicId) => {
    const [data , setData] = useState();
    const [appointments, setAppointments] = useState();
    const [customerAppointments, setCustomerAppointments] = useState();

    const getClinicData = async() =>{
        const responseData = await fetch(`/clinic/all/${clinicId}`);
        setData(await responseData.json());
        getCalendarData();
    }

    const getCalendarData = async() =>{
        const calendarData = await fetch(`/calendar/all`);
        setAppointments(await calendarData.json());
    }

    const getCustomerAppointments = async() =>{
        const appointmentsData = await fetch(`/calendar`, {
            headers: {
                'Content-Type': 'application/json',
            },body: JSON.stringify({
                //clientId:id(),
            }),
        });
        setCustomerAppointments(await appointmentsData.json());
    }

    const isDataLoaded = () => {
        console.log(!!data);
        return !!data;
    };

    useEffect(() => {
        getClinicData();
    },[data]);

    if(isDataLoaded()){
        return (
            <div className="clinic">
                {data && data.map((clinic) => {
                    <div key={clinic.name}>
                        <h1>{clinic.name}</h1>
                        <h2>Location :{clinic.zipCode} {clinic.city} {clinic.street}</h2>
                        <h3>{clinic.clinicType}</h3>
                        <h3>Open: {clinic.openingHours}</h3>
                    </div>
                })}
            </div>
        )
    } else {
        return (<Loading/>)
    }
}
