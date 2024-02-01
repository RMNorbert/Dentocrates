import "./Review.css";
import {MultiFetch} from "../../utils/fetch/MultiFetch";
import {useEffect, useState} from "react";
import {Loading} from "../lodaingPage/Loading";
import {userId} from "../../utils/token/TokenDecoder";

export const ReviewPage = ({id, byClinic} ) => {
    const { data } = MultiFetch();
    const [isDataLoaded, setDataLoaded] = useState(false);
    const [reviewData, setReviewData] = useState([]);
    const [isDeleted, setIsDelete] = useState(false);
    const reviewDataUrl = byClinic? `/review/clinic/${id}` : `/review/customer/${id}`;
    const okStatusCode = 200;
    const getReviewData = async () => {
        const responseData = await data(reviewDataUrl);
        setReviewData(await responseData);
        setDataLoaded(true)
    };

    const deleteReview = async (currentId) => {
        const reviewDeleteUrl = "/review/";
        const requestBody = {userId: userId(), targetId: currentId};
        const response =  await data(reviewDeleteUrl, "DELETE", requestBody);
        if(response.status === okStatusCode) {
            setIsDelete(true);
        }
    };

    useEffect(() => {
        if (!isDataLoaded) {
            getReviewData()
        }
    }, []);


    if (isDataLoaded) {
        return (
          <div>
              {reviewData.length > 0 && <h3 className="reviews-list">Reviews</h3>}
              {!isDeleted && reviewData && reviewData.map((review, index) => (
                  <div key={index}
                       className="review-rating-box shadowBorder"
                  >
                     <h3 className="reviewBorder">{review.reviewer}</h3>
                      {!byClinic?
                          <div className="distanceHolder">
                             <strong> {review.reviewedClinic} </strong>
                          </div>
                          :
                          <></>
                      }
                          <div>
                              <div
                                  className="review-rating upperDistanceHolder"
                              >
                              <strong> Rating:    {review.rating} </strong>
                              </div>
                              <div
                                  className="review-rating-element"
                              >
                                  {review.review}
                              </div>
                          </div>
                      {!byClinic??
                          <button
                              className="review-element"
                              onClick={()=> deleteReview(review.id)}
                          >
                              Delete review
                          </button>}
                  </div>
              ))}
          </div>
        );
    } else {
        return <Loading />;
    }
}
