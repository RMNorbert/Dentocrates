import "./TermsAndConditions.css";
import { useState } from "react";
export const Terms = (title) => {
    const block = "block";
    const none = "none";
    const [isAgreed, setIsAgreed] = useState(false);
    const [isAccepted, setIsAccepted] = useState(false);
    const [displayTermsOfUse, setDisplayTermsOfUse] = useState(block);
    const [displayIntellectualRight, setDisplayIntellectualRight] = useState(none);
    const [displayProhibited, setDisplayProhibited] = useState(none);
    const [displayGoverningLaw, setDisplayGoverningLaw] = useState(none);

    function showTab(tab) {
        switch (tab) {
            case "terms":
                setDisplayIntellectualRight(none);
                setDisplayProhibited(none);
                setDisplayGoverningLaw(none);
                setDisplayTermsOfUse(block);
                break;

            case "intellectual":
                setDisplayTermsOfUse(none);
                setDisplayProhibited(none);
                setDisplayGoverningLaw(none);
                setDisplayIntellectualRight(block);
                break;

            case "prohibited":
                setDisplayTermsOfUse(none);
                setDisplayGoverningLaw(none);
                setDisplayIntellectualRight(none);
                setDisplayProhibited(block);
                break;

            default:
                setDisplayTermsOfUse(none);
                setDisplayProhibited(none);
                setDisplayIntellectualRight(none);
                setDisplayGoverningLaw(block);
                break;

        }

    }

    return isAccepted ?
        (<></>)
        :
        (
    <div id="overlay">
        <div className="container_body">
            <div className="container">
            <div className="tabs">
                <ul>
                    <li className={displayTermsOfUse === "block" ? "active" : ""}
                        onClick={() => showTab("terms")}
                    >
                        Terms of use
                    </li>
                    <li className={displayIntellectualRight === "block" ? "active" : ""}
                        onClick={() => showTab("intellectual")}
                    >
                        Intellectual property rights
                    </li>
                    <li className={displayProhibited === "block" ? "active" : ""}
                        onClick={() => showTab("prohibited")}
                    >
                        Prohibited activities & Termination clause
                    </li>
                    <li className={displayGoverningLaw === "block" ? "active" : ""}
                        onClick={() => showTab("")}
                    >
                        Governing Law
                    </li>
                </ul>
                <h4>You must agree to the terms and conditions to continue</h4>
            </div>
            <div className="tabs_content">
                <div className="tab_head">
                    <h2>{ title.title } Terms & Conditions</h2>
                </div>
                <div className="tab_body">
                    <div className="tab_item tab_item_1"
                         style={{display: displayTermsOfUse}}
                    >
                        <h3>Terms of use</h3>
                        <p><strong>Use of Service:</strong> By using our service, you agree to comply with our terms and guidelines.</p>
                        <p><strong>Privacy:</strong> We respect your privacy and handle your data in accordance with our privacy policy.</p>
                        <p><strong>Disclaimer:</strong> The information provided is for general purposes only; we are not liable for any consequences resulting from its use.</p>
                    </div>
                    <div className="tab_item tab_item_2"
                         style={{display: displayIntellectualRight}}
                    >
                        <h3>Intellectual property rights</h3>
                        <p><strong>All content provided is protected by intellectual property laws. </strong></p>
                        <p><strong>Unauthorized use, reproduction, or distribution is prohibited without explicit consent.</strong></p>
                    </div>
                    <div className="tab_item tab_item_3"
                         style={{display: displayProhibited}}
                    >
                        <h3>Prohibited activities & Termination clause</h3>
                        <p><strong>Any engagement in prohibited activities, including misuse or violation of our guidelines, may result in immediate termination of your access to our services without prior notice.</strong></p>
                    </div>
                    <div className="tab_item tab_item_4"
                         style={{display: displayGoverningLaw}}
                    >
                        <h3>Governing law</h3>
                        <p><strong>Any disputes or claims arising from or related to our services will be governed by and construed in accordance with the laws of [Jurisdiction], without regard to its conflict of law principles.</strong></p>
                    </div>
                </div>
            </div>
            </div>
            <div className="tab_foot flex_align_justify">
                <input
                    type="checkbox"
                    id="agree"
                    onClick={() => setIsAgreed(!isAgreed)}
                />
                <label htmlFor="#agree" id="agree-check">
                    I agree
                </label>
                <button className="accept"
                        disabled={!isAgreed}
                        onClick={() => setIsAccepted(!isAccepted)}
                >
                    Accept
                </button>
            </div>
        </div>
    </div>
    )
}
