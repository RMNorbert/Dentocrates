import React, {useState} from "react";
import {MultiFetch} from "../../../../utils/fetch/MultiFetch";

export const Reset = () => {
    const [email, setEmail] = useState("");
    const [message, setMessage] = useState("")
    const { data } = MultiFetch();
    const handleEmailChange = (event) => {setEmail(event.target.value);};

    async function postResetRequest() {
        try {
            const resetUrl = "/api/reset/request";
            const requestBody = {requesterEmail: email}
            const response = await data(resetUrl, "POST", requestBody);
            if (response.status === 200) {
                setMessage(response)
            }
        } catch (e) {
            setMessage("Invalid email");
            console.error(e);
        }
    }
    return (
        <div className="inputBox">
            <label htmlFor="email">Email to send reset link:</label>
            <input
                className="shadowBorder"
                type="text" id="email"
                value={email}
                onChange={handleEmailChange}
            />
            <button
                className="shadowBorder"
                onClick={() => postResetRequest()}
            >
                Send link
            </button>
            <p display={message.length > 0 ? "block" : "none"}>{message}</p>
        </div>
    )
}
