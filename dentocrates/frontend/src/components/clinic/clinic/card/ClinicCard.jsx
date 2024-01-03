import {useNavigate} from "react-router-dom";

function ClinicCard({clinicList}) {
    const navigate = useNavigate();
    return (
        <div>
        { clinicList && clinicList.map((clinic, key) =>
                <div
                    key={key}
                    className="clinicCard shadowBorder roundBox whiteBackground upperDistanceHolder"
                >
                    <h2
                        className="halfBorder roundTop decreaseTopMargin decreaseBottomMargin darkBackground"
                    >
                        {clinic.name}
                    </h2>
                    <h3 className="halfBorder decreaseTopMargin decreaseBottomMargin">
                        {clinic.clinicType.replaceAll("_"," ")}
                    </h3>
                    <h3 className="halfBorder decreaseTopMargin decreaseBottomMargin">
                        Street Address: {clinic.street}
                    </h3>
                    <h3 className="halfBorder decreaseTopMargin decreaseBottomMargin">
                        City: {clinic.city}
                    </h3>
                    <h3 className="halfBorder decreaseTopMargin decreaseBottomMargin">
                        Postcode: {clinic.zipCode}
                    </h3>
                    <h3
                        className="halfBorder decreaseTopMargin decreaseBottomMargin"
                    >
                        Phone : {clinic.contactNumber}
                    </h3>
                    <h3>Opening hours : {clinic.openingHours}</h3>
                    <div
                        className="updateElements"
                    >
                        <button
                            className="button shadowBorder updateButton"
                            onClick={() => navigate(`/clinic/update/${clinic.id}`)}
                        >
                            Update clinic details
                        </button>
                        <button
                            className="button shadowBorder updateButton"
                            onClick={() => navigate(`/clinic/${clinic.id}`)}
                        >
                            Open clinic page
                        </button>
                    </div>
                </div>
            ) }
        </div>
    )
}
export default ClinicCard
