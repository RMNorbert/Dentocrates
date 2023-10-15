import {MultiFetch} from "../../fetch/MultiFetch";
import {useState} from "react";

export function ReviewRegister(clinicId,appointmentId) {
    const { data } = MultiFetch();
    const [isHidden, setIsHidden] = useState(true);
    const [reviewText, setReviewText] = useState('');
    const [reviewRating, setReviewRating] = useState(0);


    const handleReviewChange = (event) => {
        setReviewText(event.target.value);
    };
    const handleRatingChange = (event) => {
        setReviewRating(event.target.value);
    };
    const updateReviewStateOfAppointment = async () => {
        const reviewAppointmentUrl = '/calendar/register';
        const idOfAppointment = {id:appointmentId}
        const response = await data(reviewAppointmentUrl,'POST',idOfAppointment)
    };
    const registerReview = async () => {
        const registerReviewUrl = '/review/register';
        let requestBody = {
            reviewerId: userId(),
            reviewedClinicId: clinicId,
            reviewedAppointmentId: appointmentId,
            rating:reviewRating,
            review:reviewText
        }
        const response = await data(registerReviewUrl,'POST',requestBody)

    };

    return (
        <div>
            <button
                onClick={() => setIsHidden(!isHidden)}
                style={{ display: isHidden ? "block" : "none" }}
            >
                Review
            </button>
            <div
                style={{ display: isHidden ? "none" : "block" }}
            >
                <input
                type={"text"}
                id="name"
                value={reviewText}
                onChange={handleReviewChange}
                />
                <h1>Rate the clinic:</h1>
                <form >
                    <label>
                        <input type="radio"
                               name="rating"
                               value="0"
                               checked={reviewRating === 0}
                               onChange={handleRatingChange}
                        />
                        0
                    </label>
                    <br/>
                    <label>
                        <input type="radio"
                               name="rating"
                               value="1"
                               checked={reviewRating === 1}
                               onChange={handleRatingChange}
                        />
                        1
                    </label>
                    <br/>
                    <label>
                        <input type="radio"
                               name="rating"
                               value="2"
                               checked={reviewRating === 2}
                               onChange={handleRatingChange}
                        />
                        2
                    </label>
                    <br/>
                    <label>
                        <input type="radio"
                               name="rating"
                               value="3"
                               checked={reviewRating === 3}
                               onChange={handleRatingChange}
                        />
                        3
                    </label>
                    <br/>
                    <label>
                        <input type="radio"
                               name="rating"
                               value="4"
                               checked={reviewRating === 4}
                               onChange={handleRatingChange}
                        />
                        4
                    </label>
                    <br/>
                    <label>
                        <input type="radio"
                               name="rating"
                               value="5"
                               checked={reviewRating === 5}
                               onChange={handleRatingChange}
                        />
                        5
                    </label>
                    <br/>
                    <button className="appointment-delete"
                            onClick={() => registerReview()}
                    >
                        Submit Review
                    </button>
                </form>
            </div>
        </div>
    )
}
