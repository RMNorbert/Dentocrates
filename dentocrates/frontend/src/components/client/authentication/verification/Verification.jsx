import "../../ClientPage.css"
import {useEffect, useState} from "react";
import { email } from "../../../../utils/token/TokenDecoder";
import {useNavigate, useParams} from "react-router-dom";
import { MultiFetch } from "../../../../utils/fetch/MultiFetch";
function VerifyPage (props) {
    const navigate = useNavigate();
    const { verificationCode } = useParams();
    const { data } = MultiFetch();
    const [verificationCodeValidated, setVerificationCodeValidated] = useState(false);
    const [password, setPassword] = useState("");
    const [passwordVerifier, setPasswordVerifier] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [message, setMessage] = useState("");

    const validateVerificationCode = async () => {
        const requestBody = {verificationCode: verificationCode, email: email()}
        const response = await data(`/verify/${verificationCode}`,"POST", requestBody);
        setVerificationCodeValidated(response);
        if(!props.reset){
                deleteVerification();
        }
    }
    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };
    const handlePasswordVerifierChange = (event) => {
        setPasswordVerifier(event.target.value);
    };
    const deleteVerification = async () => {
        const deleteVerifyUrl = "/verify/";
        const deleteRequest = {verificationCode: verificationCode};
        await data(deleteVerifyUrl, "DELETE", deleteRequest);
    }
    const postPasswordReset = async(password)=>{
        if(password === passwordVerifier) {
            const clientUrl = "/update/password";
            const request = {
                verificationCode: verificationCode,
                email: email(),
                password: password
            };
            const response = await data(clientUrl, "POST", request);
            if (response.includes("successfully")) {
                await deleteVerification();
                navigate("/home");
            }
        }
        setMessage("Passwords do not match");
    };
    const postClientVerification = async ()=>{
            const verificationUrl = "/update/verify";
            const request = {
                verificationCode: verificationCode,
            };
            const response = await data(verificationUrl, "Post", request);
            if (response.includes("successful")) {
                await deleteVerification();
            }
        };

    useEffect(() => {
        validateVerificationCode();
        if(!props.reset) {
            postClientVerification();
        }
        }, [verificationCodeValidated]);

    if(!props.reset && verificationCodeValidated) {
        return(
        <div className="client-box">
            <div className="client-card"
            >
                Your email has been verified
            </div>
        </div>
        )
    } else if (verificationCodeValidated){
        return (
            <div className="client-box">
                <div className="client-card">
                   <div>
                        <h5>
                            New password
                        </h5>
                       <input
                        type={showPassword ? "text" : "password"}
                        onChange={(e) => handlePasswordChange(e)}
                       />
                       <div>
                       <button
                           onClick={() => setShowPassword(!showPassword)}
                       >
                           Show password
                       </button>
                       </div>
                        <h5>
                            Repeat new password:
                        </h5>
                        <input
                           type={showPassword ? "text" : "password"}
                           onChange={(e) => handlePasswordVerifierChange(e)}
                        />
                       <div>
                           {message.length === 0 ? "***" : message}
                       </div>
                    </div>
                    <div>
                        <button
                            onClick={() => postPasswordReset(password)}
                        >
                            Set new password
                        </button>
                    </div>
                </div>
            </div>
        )
    }
}

export default VerifyPage;
