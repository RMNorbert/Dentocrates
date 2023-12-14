import ReactCalendar from "react-calendar";
import "./Calendar.css";
import {useEffect, useState} from "react";
import {add, format} from "date-fns";
import { userId } from "../../../utils/token/TokenDecoder";
import {useNavigate, useParams} from "react-router-dom";
import {MultiFetch} from "../../../utils/fetch/MultiFetch";
import {Loading} from "../../lodaingPage/Loading";
const Calendar = () => {
    const { id } = useParams();
    const { data } = MultiFetch();
    const navigate = useNavigate();
    const [isLoaded,setIsLoaded] = useState(false);
    const [leaveDates, setLeaveDates] = useState([]);
    const [clinic, setClinic] = useState([]);
    const [appointments, setAppointments] = useState([]);
    const [date, setDate] = useState({justDate:null,dateTime:null});
    const [errorMessage, setErrorMessage] = useState('');
    const [isHidden, setIsHidden] = useState(true);
    const getCalendarData = async() =>{
        const clinicDataUrl = `/dentocrates/clinic/${id}`;
        const clinicCalendarDataUrl = `/dentocrates/calendar/clinic/${id}`;
        const clinicLeaveDataUrl = `/dentocrates/leave/${id}`;

        const clinicData = await data(clinicDataUrl);
        const calendarData = await data(clinicCalendarDataUrl);
        const leaveData = await data(clinicLeaveDataUrl);
        setClinic(clinicData);
        setAppointments(await calendarData);
        getAllLeaveDates(await leaveData);
    }

    const getAllLeaveDates = (leaveDates) => {
        const dates = [];
        const startDateModifier = 1;

        for(let i = 0; i < leaveDates.length; i++){
            let startDate = new Date(leaveDates[i].startOfTheLeave);
            let endDate = new Date(leaveDates[i].endOfTheLeave)

            while (startDate <= endDate) {
                dates.push(startDate.getDate());
                startDate.setDate(startDate.getDate() + startDateModifier);
            }
        }
        setLeaveDates(dates);
    }
    const getTimes = () => {
        if (!date.justDate) {return}
        const { justDate } = date;
        const openingHours = clinic.openingHours.split("-");
        const open = Number(openingHours[0]);
        const close = Number(openingHours[1]);

        const beginning = add(justDate, { hours: open });
        const end = add(justDate, { hours: close });
        const interval = 30; //minutes
        const currentTime = new Date();

        const currentDate = beginning.toJSON().substring(0,10);
        const bookedAppointments = appointments.filter((date) =>
                                            date.reservation.substring(0,10) === currentDate)
                                            .map((obj) => obj.reservation.substring(11,16));
        const times = [];
        for(let i = beginning; i <= end; i = add(i, {minutes: interval})){
            const formattedTime = format(i, 'kk:mm');
            if (!bookedAppointments.includes(formattedTime) && i >= currentTime) {
                times.push(i);
            }
        }
        return times;
    }
    const bookAppointment = async(appointment) => {
        try {
        const calendarRegisterUrl = '/dentocrates/calendar/register';
        const formattedAppointment  = appointment.replace(" ","T") + ":00";
        const bookingData = {
            clinicId: id,
            customerId: userId(),
            reservation: formattedAppointment
        };

        const response = await data(calendarRegisterUrl, 'POST', bookingData);
        setIsLoaded(false);
        if(response.includes('successfully')){
            navigate(`/clinic/${id}`);
        }
        } catch (error) {
            console.error('Error:', error);
        }
    }
    const noDateChosen = () => {
        setErrorMessage("No appointment chosen");
        setIsHidden(false);
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
                            <div key={`time-${i}`} >
                                <button type="button"
                                        className="times"
                                        onClick={() => setDate((prev) => ({...prev, dateTime: time}))}
                                >
                                    {format(time, 'kk:mm')}
                                </button>
                            </div>
                        ))}
                        <button
                            onClick={() =>
                                date.dateTime != null ?
                                bookAppointment(date.dateTime.toISOString()
                                    .substring(0, 16).replace("T", " "))
                                    :
                                noDateChosen()
                        }
                        >Book
                            Appointment
                        </button>
                        <p className="message-box" hidden={isHidden}>{errorMessage}</p>
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
