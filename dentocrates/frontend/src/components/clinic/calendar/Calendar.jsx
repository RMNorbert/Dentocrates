import ReactCalendar from "react-calendar";

const Calendar = () => {
    return (
      <div>
        <ReactCalendar minDate={new Date()}
         className="REACT-CALENDAR p-2"
         view="month"
         onClickDay={(date) =>console.log(date)}
        />
      </div>
    );
  };
  
  export default Calendar;