import ReactCalendar from "react-calendar";
import "./Calendar.css";
import {useState} from "react";
import {add, format} from "date-fns";
import { userId } from "../../token/TokenDecoder";
import { useParams } from "react-router-dom";
import {MultiFetch} from "../../../fetch/MultiFetch";
const Calendar = (openingHours) => {
    const { id } = useParams();
    const { data } = MultiFetch();
    const [appointments, setAppointments] = useState([]);
    const [date, setDate] = useState({justDate:null,dateTime:null});
    const getCalendarData = async() =>{
        const calendarData = await fetch(`/calendar//clinic/${id}`);
        setAppointments(await calendarData.json());
    }

    const getTimes = () => {
        if (!date.justDate) {return}

        const { justDate } = date;

        const beginning = add(justDate, { hours: 9 });
        const end = add(justDate, { hours: 17 });
        const interval = 30; //minutes

        const times = [];
        for(let i = beginning; i <= end; i = add(i, {minutes: interval})){
            times.push(i);
        }
        return times;
    }

    const bookAppointment = (appointment) => {
        const bookingData = {
            clinicId: id,
            customerId: userId(),
            reservation: appointment
        };
        data('/calendar/register', 'POST', bookingData);
    }

    const times = getTimes();

    return (
      <div className="cal" >
          {date.justDate ? (
              <div className="date">
                  {times?.map((time, i) => (
                      <div key={`time-${i}`} className="times">
                        <button type="button"
                                onClick={() => setDate((prev) => ({...prev, dateTime: time }))}
                        >
                            {format(time, 'kk:mm')}
                        </button>
                      </div>
                  ))}
                  <button onClick={() => bookAppointment(date.dateTime.toISOString().substring(0, 16).replace("T"," "))}>Book Appointment</button>
              </div>
              ):(
          <ReactCalendar minDate={new Date()}
         className="REACT-CALENDAR p-2"
         view="month"
         onClickDay={(date) => setDate((prev) => ({...prev, justDate: date}))}
        />
          )}
      </div>
    );
  };

  export default Calendar;
