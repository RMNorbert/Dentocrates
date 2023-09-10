import "./ClinicPage.css";
import React, { useEffect, useState } from "react";
import { Loading } from "../elements/Loading";
import { role , userId} from "../token/TokenDecoder";
import { MultiFetch, ApiResponse } from "../../fetch/MultiFetch";
import { useParams, useNavigate } from "react-router-dom";


export const ClinicPage = () => {
    const { id } = useParams<{ id: string }>();
    const { data } = MultiFetch();
    const currentDate = new Date().toJSON();
    const navigate = useNavigate();
    const [isLoaded, setDataLoaded] = useState<boolean>(false);
    const [isFetched, setIsFetched] = useState<boolean>(false);
    const [clinicData, setClinicData] = useState<ClinicResponseDTO | null>(null);
    const [dentistData, setDentistData] = useState<DentistResponseDTO | null>(null);
    const [appointments, setAppointments] = useState<AppointmentDTO[]>([]);
    const [customerAppointments, setCustomerAppointments] = useState<CustomerAppointmentResponseDTO[]>([]);
    const [customers, setCustomers] = useState<ApiResponse[]>([]);


    const getClinicData = async () => {
        const clinicDataUrl = `/clinic/${id}`;
        const dentistDataUrl = `/dentist/`;
        const responseData = await data(clinicDataUrl);
        setClinicData(responseData);
        const dentistResponse = await data(dentistDataUrl + responseData.dentistId);
        setDentistData(dentistResponse);
    };
    const updateAppointment = async (currentId: number, appearance: boolean) => {
        const calendarDataUrl = '/calendar/';
        const requestBody = { id: currentId, clinicId: id, dentistId: userId(), appeared: !appearance };
        await data(calendarDataUrl, 'PUT', requestBody);
    };

    const getCalendarData = async () => {
        const clinicCalendarDataUrl = `/calendar/clinic/${id}`;
        const calendarData = await data(clinicCalendarDataUrl);
        const appointmentsData = await calendarData.filter((appointment: AppointmentDTO) =>
            appointment.reservation >= currentDate).sort(
            (a: any, b: any) => Number(a.reservation.substring(0, 19)
                    .replace(/[T:-]/g, ''))
                -
                Number(b.reservation.substring(0, 19)
                    .replace(/[T:-]/g, ''))
        );

        setAppointments(await appointmentsData);
    };

    const getCustomerDetails = async () => {
        const customersDataUrl = '/client/all';
        const responseData = await data(customersDataUrl);
        setCustomers(responseData);
    };

    const filterCustomerDetails = (customerId: number) => {
        return customers.filter((customer: any) => customer.id === customerId)
            .map((customer: any, index: number) => (
                <div key={index}>
                    <h2 className="listName listMargin">Name: {customer.firstName} {customer.lastName}</h2>
                </div>
            ));
    };

    const dateFormatter = (date: string) => {
        return date.substring(0, 16).replace('T', ' ');
    };

    const getCustomerAppointments = async () => {
        const customerAppointmentsDataUrl = `/calendar/customer/${userId()}`;
        const appointmentsData = await data(customerAppointmentsDataUrl);
        const sortedAppointments = await appointmentsData.filter((appointment: AppointmentDTO) =>
            appointment.clinicId === Number(id) && appointment.reservation >= currentDate).sort(
            (a: any, b: any) => Number(a.reservation.substring(0, 19)
                    .replace(/[T:-]/g, ''))
                -
                Number(b.reservation.substring(0, 19)
                    .replace(/[T:-]/g, ''))
        );
        setCustomerAppointments(sortedAppointments);
    };

    const fetchData = async () => {
        if (role() === 'DENTIST') {
            await getCustomerDetails();
            await getCalendarData();
            await getClinicData();
        } else if (role() === 'CUSTOMER') {
            await getCustomerAppointments();
            await getClinicData();
        }
        setIsFetched(true);
    };

    useEffect(() => {
        if (!isFetched && !isLoaded) {
            fetchData();
        } else if (isFetched) {
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
                        <h3>{clinicData.clinicType.replaceAll('_', ' ')}</h3>
                        <h3>Open: {clinicData.openingHours}</h3>
                        <h3> Dentist: Dr. {dentistData && dentistData ? `${dentistData.firstName} ${dentistData.lastName}` : 'N/A'}</h3>
                    </div>
                ) : (
                    <Loading />
                )}
                {role() === 'DENTIST' && clinicData && clinicData.dentistId === userId() ? (
                    appointments.map((appointment: any, index: number) => (
                        <div key={index} className="listName listMargin customer-box">
                            {dateFormatter(appointment.reservation)}
                            <input
                                className="fulfilled"
                                type="checkbox"
                                onChange={() => updateAppointment(appointment.id, appointment.appeared)}
                            />
                            {filterCustomerDetails(appointment.customerId)}
                        </div>
                    ))
                ) : (
                    <div>
                        <button className="button" onClick={() => navigate(`/calendar/${id}`)}>Book an appointment</button>
                        {customerAppointments.map((appointment: any, index: number) => (
                            <div key={index} className="customer">
                                <div className="customer-appointment">
                                    {appointment.reservation.replace('T', ' ')}
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
