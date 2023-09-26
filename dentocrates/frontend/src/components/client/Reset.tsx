import React, {ChangeEvent, useState} from "react";
import {MultiFetch} from "../../fetch/MultiFetch";

export const Reset = () => {
    const [requesterEmail, setRequesterEmail] = useState<string>("");
    const handleEmailChange = (event:ChangeEvent<HTMLInputElement>) => {setRequesterEmail(event.target.value);};

    async function postResetRequest() {
        const resetUrl = "/api/request/authenticate";
        const requestBody = {email: requesterEmail}
        await MultiFetch(resetUrl,"POST",requestBody);
    }
    return (
        <div>
            <label htmlFor="email">Email to send reset link:</label>
            <input type="text" id="email" value={requesterEmail} onChange={handleEmailChange} />
            <button onClick={() => postResetRequest()}>Send link</button>
        </div>
    )
}
