import {MultiFetch} from "../../fetch/MultiFetch";
import {useEffect, useState} from "react";
import {Loading} from "../lodaingPage/Loading";
import { userId} from "../token/TokenDecoder";

export const ReviewPage = (id,byClinic ) => {
    const { data } = MultiFetch();
    const [isDataLoaded, setDataLoaded] = useState(false);
    const [reviewData, setReviewData] = useState([]);
    const [isDeleted, setIsDelete] = useState(false);
    const getReviewData = async () => {
        const reviewDataUrl = byClinic? `/review/${id}` : `/customer/${id}`;
        const responseData = await data(reviewDataUrl);
        setReviewData(await responseData);
        setDataLoaded(true)
    };

    const deleteReview = async (currentId) => {
        const reviewDeleteUrl = "/review/";
        const requestBody = {userId: userId(), targetId: currentId};
        const response =  await data(reviewDeleteUrl, 'DELETE', requestBody);
        if(response.status === 200) {
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
                       className="review"
                  >
                     <h1>{review.reviewer}</h1>
                      {!byClinic?
                          <div>
                              {review.reviewedClinic}
                          </div>
                          :
                          <></>
                      }
                          <div>
                              <div>{review.rating}</div>
                              <div>
                                  {review.review}
                              </div>
                          </div>
                      {!byClinic??
                          <button onClick={()=> deleteReview(review.id)}>
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
