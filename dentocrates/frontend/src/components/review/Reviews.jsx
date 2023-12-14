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
    const reviewDataUrl = byClinic? `/dentocrates/review/clinic/${id}` : `/dentocrates/review/customer/${id}`;
    const okStatusCode = 200;
    const getReviewData = async () => {
        const responseData = await data(reviewDataUrl);
        setReviewData(await responseData);
        setDataLoaded(true)
    };

    const deleteReview = async (currentId) => {
        const reviewDeleteUrl = '/dentocrates/review/';
        const requestBody = {userId: userId(), targetId: currentId};
        const response =  await data(reviewDeleteUrl, 'DELETE', requestBody);
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
              {!isDeleted && reviewData && reviewData.map((review, index) => (
                  <div key={index}
                       className="review-rating-box"
                  >
                     <h3>{review.reviewer}</h3>
                      {!byClinic?
                          <div>
                              {review.reviewedClinic}
                          </div>
                          :
                          <></>
                      }
                          <div>
                              <div
                                  className="review-rating"
                              >
                              Rating:    {review.rating}
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
