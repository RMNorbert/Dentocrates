import React, { useState} from "react";
import {MultiFetch} from "../../../utils/fetch/MultiFetch";
import {userId} from "../../../utils/token/TokenDecoder";

function AppointmentCard (props) {
    const { data } = MultiFetch();
    const [appeared, setIsAppeared] = useState(props.appointments.appeared)
    const updateAppointment = async (currentId, appearance) => {
        const calendarDataUrl = "/calendar/";
        const requestBody = {id:currentId, clinicId: props.id, dentistId: userId(), appeared:!appearance};
        await data(calendarDataUrl,"PUT", requestBody);
        setIsAppeared(!appeared);
    }
    const filterCustomerDetails = (customerId) => {
        return props.customers.filter((customer) => customer.id === customerId)
            .map((customer,index) => (
                <div key={index}>
                    <h2 className="listName listMargin">Name: {customer.firstName} {customer.lastName}</h2>
                </div>
            ))
    };

        return (
            <>
            <div
                className="listName listMargin customer-box shadowBorder roundBox distanceHolder"
                id={appeared ? "appeared" : "not-appeared"}
            >
               <strong>{props.dateFormatter(props.appointments.reservation)}</strong>
                <button
                    className="fulfilled"
                    onClick={() => updateAppointment(props.appointments.id, props.appointments.appeared)}
                >{appeared ? "not-appeared" : "appeared"}</button>
                {filterCustomerDetails(props.appointments.customerId)}
            </div>
            </>
        )
}

export default AppointmentCard;
