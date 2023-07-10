import ReactCalendar from "react-calendar";
import "./Calendar.css";
import {useEffect, useState} from "react";
import {add, format} from "date-fns";
import { userId } from "../../token/TokenDecoder";
import { useParams } from "react-router-dom";
import {MultiFetch} from "../../../fetch/MultiFetch";
import {Loading} from "../../elements/Loading";
const Calendar = (openingHours) => {
    const { id } = useParams();
    const { data } = MultiFetch();
    const [isLoaded,setIsLoaded] = useState(false);
    const [appointments, setAppointments] = useState([]);
    const [leaveDates, setLeaveDates] = useState([]);
    const [date, setDate] = useState({justDate:null,dateTime:null});
    const getCalendarData = async() =>{
        const calendarData = await data(`/calendar/clinic/${id}`);
        const leaveData = await data(`/leave/${id}`);
        setAppointments(await calendarData);
        getAllLeaveDates(await leaveData);
    }

    const getAllLeaveDates = (leaveDates) => {
        const dates = [];
        for(let i = 0; i < leaveDates.length; i++){
            let startDate = new Date(leaveDates[i].startOfTheLeave);
            let endDate = new Date(leaveDates[i].endOfTheLeave)

            while (startDate <= endDate) {
                dates.push(startDate.getDate());
                startDate.setDate(startDate.getDate() + 1);
            }
        }
        setLeaveDates(dates);
    }
    const getTimes = () => {
        if (!date.justDate) {return}

        const { justDate } = date;

        const beginning = add(justDate, { hours: 9 });
        const end = add(justDate, { hours: 17 });
        const interval = 30; //minutes

        const currentDate = beginning.toJSON().substring(0,10);
        const bookedAppointments = appointments.filter((date) =>
                                            date.reservation.substring(0,10) === currentDate)
                                            .map((obj) => obj.reservation.substring(11,16));
        const times = [];
        for(let i = beginning; i <= end; i = add(i, {minutes: interval})){
            const formattedTime = format(i, 'kk:mm');
            if (!bookedAppointments.includes(formattedTime)) {
                times.push(i);
            }
        }
        return times;
    }

    const bookAppointment = async(appointment) => {
        const formattedAppointment  = appointment.replace(" ","T") + ":00";
        const bookingData = {
            clinicId: id,
            customerId: userId(),
            reservation: formattedAppointment
        };
        await data('/calendar/register', 'POST', bookingData);
        setIsLoaded(false);
    }


    useEffect(() => {
        if(!isLoaded){
            getCalendarData();
            setIsLoaded(true);
        }
    },[isLoaded])
    if(isLoaded) {
        const times = getTimes();
        return (
            <div className="cal">
                {date.justDate ? (
                    <div className="date">
                        {times?.map((time, i) => (
                            <div>
                                <button type="button"
                                        key={`time-${i}`} className="times"
                                        onClick={() => setDate((prev) => ({...prev, dateTime: time}))}
                                >
                                    {format(time, 'kk:mm')}
                                </button>
                            </div>
                        ))}
                        <button
                            onClick={() => bookAppointment(date.dateTime.toISOString().substring(0, 16).replace("T", " "))}>Book
                            Appointment
                        </button>
                    </div>
                ) : (
                    <ReactCalendar minDate={new Date()}
                                   className="REACT-CALENDAR p-2"
                                   view="month"
                                   onClickDay={(date) => setDate((prev) => ({...prev, justDate: date}))}
                                   tileDisabled={({date}) => leaveDates.includes(date.getDate())}
                    />
                )}
            </div>
        );
    } else {
        return (<Loading/>);
    }
  };

  export default Calendar;
