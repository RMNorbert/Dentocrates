import React, {useState} from "react";
import {MultiFetch} from "../../../../utils/fetch/MultiFetch";

export const Reset = () => {
    const [requesterEmail, setRequesterEmail] = useState("");
    const { data } = MultiFetch();
    const handleEmailChange = (event) => {setRequesterEmail(event.target.value);};

    async function postResetRequest() {
        const resetUrl = "/api/request/authenticate";
        const requestBody = {email: requesterEmail}
        await data(resetUrl,"POST",requestBody);
    }
    return (
        <div>
            <label htmlFor="email">Email to send reset link:</label>
            <input type="text" id="email" value={requesterEmail} onChange={handleEmailChange} />
            <button onClick={() => postResetRequest()}>Send link</button>
        </div>
    )
}
