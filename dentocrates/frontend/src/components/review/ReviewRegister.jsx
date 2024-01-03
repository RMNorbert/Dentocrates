import "./ReviewRegister.css";
import {MultiFetch} from "../../utils/fetch/MultiFetch";
import {useState} from "react";
import {userId} from "../../utils/token/TokenDecoder";
import {useNavigate} from "react-router-dom";

export function ReviewRegister({clinicId, appointmentId}) {
    const { data } = MultiFetch();
    const navigate = useNavigate();
    const [isHidden, setIsHidden] = useState(true);
    const [isReviewed, setIsReviewed] = useState(false);
    const [reviewText, setReviewText] = useState("");
    const [reviewRating, setReviewRating] = useState(0);


    const handleReviewChange = (event) => {
        setReviewText(event.target.value);
    };
    const handleRatingChange = (event) => {
        setReviewRating(event.target.value);
    };
    const updateReviewStateOfAppointment = async () => {
        const reviewAppointmentUrl = "/calendar/";
        const response = await data(reviewAppointmentUrl,"POST",appointmentId)
        if(response.includes("successfully")){
            setIsHidden(true);
            navigate("/appointments")
        }
    };
    const registerReview = async () => {
        const registerReviewUrl = "/review/register";
        let requestBody = {
            reviewerId: userId(),
            reviewedClinicId: clinicId,
            reviewedAppointmentId: appointmentId,
            rating:reviewRating,
            review:reviewText
        }
        const response = await data(registerReviewUrl,"POST",requestBody);
        if(response.includes("successfully")) {
            await updateReviewStateOfAppointment();
            setIsReviewed(true)
        }
    };

    return (
        <div className="review-box"
             style={{display: isReviewed ? "none" : "block" }}
        >
            <div className="review-element">
            <button className="review"
                onClick={() => setIsHidden(!isHidden)}
                style={{display: isHidden ? "block" : "none" }}
            >
                Review
            </button>
            <div
                style={{ display: isHidden ? "none" : "block" }}
                className="review-element"
            >
                <textarea
                className="review-input shadowLightBorder distanceHolder"
                id="name"
                value={reviewText}
                onChange={handleReviewChange}
                />
                <h4 className="h4">Rate the clinic:</h4>
                <div className="radio">
                    <label className="rating">
                        <input type="radio"
                               name="rating"
                               className="radio"
                               value="0"
                               onChange={(e) => handleRatingChange(e)}
                        />
                        0
                    </label>
                    <label className="rating">
                        <input type="radio"
                               name="rating"
                               className="radio"
                               value="1"
                               onChange={(e) => handleRatingChange(e)}
                        />
                        1
                    </label>
                    <br/>
                    <label className="rating">
                        <input type="radio"
                               name="rating"
                               className="radio"
                               value="2"
                               onChange={(e) => handleRatingChange(e)}
                        />
                        2
                    </label>
                    <br/>
                    <label className="rating">
                        <input type="radio"
                               name="rating"
                               className="radio"
                               value="3"
                               onChange={(e) => handleRatingChange(e)}
                        />
                        3
                    </label>
                    <br/>
                    <label className="rating">
                        <input type="radio"
                               name="rating"
                               className="radio"
                               value="4"
                               onChange={(e) => handleRatingChange(e)}
                        />
                        4
                    </label>
                    <br/>
                    <label className="rating">
                        <input type="radio"
                               name="rating"
                               className="radio"
                               value="5"
                               onChange={(e) => handleRatingChange(e)}
                        />
                        5
                    </label>
                    <br/>
                    <button className="appointment-delete"
                            onClick={() => registerReview()}
                    >
                        Submit Review
                    </button>
                </div>
            </div>
            </div>
        </div>
    )
}
