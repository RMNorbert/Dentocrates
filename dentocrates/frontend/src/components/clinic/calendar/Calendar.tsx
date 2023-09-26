import ReactCalendar from "react-calendar";
import "./Calendar.css";
import React, {useEffect, useState} from "react";
import {add, format} from "date-fns";
import { userId } from "../../token/TokenDecoder";
import { useParams } from "react-router-dom";
import {MultiFetch} from "../../../fetch/MultiFetch";
import {Loading} from "../../elements/Loading";


const Calendar = () => {
    const { id } = useParams<{ id: string }>();
    const [isLoaded, setIsLoaded] = useState<boolean>(false);
    const [leaveDates, setLeaveDates] = useState<number[]>([]);
    const [clinic, setClinic] = useState<ClinicResponseDTO | null>(null);
    const [appointments, setAppointments] = useState<AppointmentDTO[]>([]);
    const [date, setDate] = useState<{ justDate: Date | null; dateTime: Date | null }>({
        justDate: null,
        dateTime: null,
    });

    const getCalendarData = async () => {
        const clinicDataUrl = `clinic/${id}`;
        const clinicCalendarDataUrl = `/calendar/clinic/${id}`;
        const clinicLeaveDataUrl = `/leave/${id}`;

        const [clinicData, calendarData, leaveData] = await Promise.all([
            MultiFetch<ClinicResponseDTO>(clinicDataUrl),
            MultiFetch<AppointmentDTO[]>(clinicCalendarDataUrl),
            MultiFetch<LeaveDTO[]>(clinicLeaveDataUrl),
        ]);

        setClinic(clinicData);
        setAppointments(calendarData);
        getAllLeaveDates(leaveData);
    };

    const getAllLeaveDates = (leaveDates: LeaveDTO[]) => {
        const dates: number[] = [];
        const startDateModifier = 1;

        for (let i = 0; i < leaveDates.length; i++) {
            let startDate = new Date(leaveDates[i].startOfTheLeave);
            let endDate = new Date(leaveDates[i].endOfTheLeave);

            while (startDate <= endDate) {
                dates.push(startDate.getDate());
                startDate.setDate(startDate.getDate() + startDateModifier);
            }
        }
        setLeaveDates(dates);
    };

    const getTimes = () => {
        if (!date.justDate) {
            return [];
        }
        const { justDate } = date;
        const openingHours = clinic?.openingHours?.split('-');
        const open = Number(openingHours?.[0] ?? 0);
        const close = Number(openingHours?.[1] ?? 0);

        const beginning = add(justDate, { hours: open });
        const end = add(justDate, { hours: close });
        const interval = 30; // minutes
        const currentTime = new Date();

        const currentDate = beginning.toJSON().substring(0, 10);
        const bookedAppointments = appointments
            .filter((date) => date.reservation.substring(0, 10) === currentDate)
            .map((obj) => obj.reservation.substring(11, 16));
        const times: Date[] = [];
        for (let i = beginning; i <= end; i = add(i, { minutes: interval })) {
            const formattedTime = format(i, 'kk:mm');
            if (!bookedAppointments.includes(formattedTime) && i >= currentTime) {
                times.push(i);
            }
        }
        return times;
    };

    const bookAppointment = async (appointment: string) => {
        try {
            const calendarRegisterUrl = '/calendar/register';
            const formattedAppointment = appointment.replace(' ', 'T') + ':00';
            const bookingData = {
                clinicId: id,
                customerId: userId(),
                reservation: formattedAppointment,
            };

            await MultiFetch(calendarRegisterUrl, 'POST', bookingData);
            setIsLoaded(false);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    useEffect(() => {
        if (!isLoaded) {
            getCalendarData();
            setIsLoaded(true);
        }
    }, [isLoaded]);

    if (isLoaded) {
        const times = getTimes();
        return (
            <div className="cal">
                {date.justDate ? (
                    <div className="date">
                        {times?.map((time, i) => (
                            <div key={`time-${i}`}>
                                <button
                                    type="button"
                                    className="times"
                                    onClick={() => setDate((prev) => ({ ...prev, dateTime: time }))}
                                >
                                    {format(time, 'kk:mm')}
                                </button>
                            </div>
                        ))}
                        <button
                            onClick={() =>
                                date.dateTime != null
                                    ? bookAppointment(
                                        date.dateTime.toISOString().substring(0, 16).replace('T', ' ')
                                    )
                                    : console.log('No date chosen')
                            }
                        >
                            Book Appointment
                        </button>
                    </div>
                ) : (
                    <ReactCalendar
                        minDate={new Date()}
                        className="REACT-CALENDAR p-2"
                        view="month"
                        onClickDay={(date) => setDate((prev) => ({ ...prev, justDate: date }))}
                        tileDisabled={({ date }) => leaveDates.includes(date.getDate())}
                    />
                )}
            </div>
        );
    }  else {
        return (<Loading/>);
    }
  };

  export default Calendar;
