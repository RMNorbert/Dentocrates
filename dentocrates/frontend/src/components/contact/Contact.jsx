import "./Contact.css"
import React, {useState} from "react";
import { MultiFetch } from "../../utils/fetch/MultiFetch";
function Contact() {
    const { data } = MultiFetch();
    const email = "dentocrates@dentocrates.dentocrates"
    const phone = "0-1-001-001-01-01"
    const [senderEmail, setSenderEmail] = useState("");
    const [subject, setSubject] = useState("");
    const [message, setMessage] = useState("");
    const [isMessageBoxHidden, setIsMessageBoxHidden] = useState(false);
    const errorMessage = "The email must be valid and every field must be filled out.";
    const [isErrorMessageHidden, setIsErrorMessageHidden] = useState(true);
    const handleSenderEmailChange = (event) => {
        setSenderEmail(event.target.value);
    };

    const handleSubjectChange = (event) => {
        setSubject(event.target.value);
    };
    const handleMessageChange = (event) => {
        setMessage(event.target.value);
    };
    const postMessage = async()=>{
        const contactMessageRegisterUrl = "/contact";
        let requestBody = {
            sender: senderEmail,
            subject: subject,
            message: message
        }
        try {
            if(senderEmail !== "" && subject !=="" && message !== "") {
                const response = await data(contactMessageRegisterUrl,"POST", requestBody);
                if(response === true) {
                    setIsErrorMessageHidden(true);
                    setIsMessageBoxHidden(true);
                }
            } else {
                setIsErrorMessageHidden(false);
            }
        } catch (error) {
            setIsMessageBoxHidden(false);
            console.error("Error:", error);
            setIsErrorMessageHidden(false);
        }
    };

    const HandleSubmit = async (e) => {
        e.preventDefault();
        await postMessage();
    }

    return (
        <div className='contact'>
            <div className='contact-content-box shadowBorder'>
                <h1>Contact us by:</h1>
                <h3 className='contactDescription'>
                    Email : {email}
                </h3>
                <h3 className='phone'>
                    Phone: {phone}
                </h3>
            </div>
                <div
                    className="contact-content-box shadowBorder"
                    style={{ display: isMessageBoxHidden ? "none" : "block" }}
                >
                <h3>Or just provide a message here</h3>
                    <form onSubmit={HandleSubmit}>
                        <div className="inputBox">
                            <label htmlFor="senderEmail">Your contact email:</label>
                            <input
                                className="shadowBorder distanceHolder"
                                type="text"
                                value={senderEmail}
                                onChange={handleSenderEmailChange}
                            />
                        </div>
                        <div className="inputBox">
                            <label htmlFor="subject">Subject:</label>
                            <input
                                className="shadowBorder distanceHolder"
                                type="text"
                                value={subject}
                                onChange={handleSubjectChange}
                            />
                        </div>
                        <div className="inputBox">
                            <label htmlFor="message">Message:</label>
                            <textarea
                                className="shadowBorder"
                                value={message}
                                onChange={handleMessageChange}
                            />
                        </div>
                        <button className="inputBox shadowBorder" type="submit">
                            Register
                        </button>
                    </form>
                </div>
            <div
                className="contact-content-box shadowBorder"
                style={{ display: isMessageBoxHidden ? "block" : "none"}}
            >
                <h3>We got your message, we will reply soon!</h3>
            </div>
            <div
                className="contact-content-box shadowBorder"
                style={{ display: isErrorMessageHidden ? "none" : "block"}}
            >
                <strong>{errorMessage}</strong>
            </div>
            </div>
    )
}

export default Contact;
