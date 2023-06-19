import { useNavigate } from "react-router-dom";
export const ClinicList = ({ clinicDatas }) => {
    const navigate = useNavigate();
    return (
        <div>
            {clinicDatas &&
                clinicDatas.map((clinic) => {
                    return (
                        <div
                            key={clinic.name}
                            className="listBox"
                            onClick={() => navigate(`/clinic/${clinic.id}`)}
                        >
                            <h1 className="listName listMargin">{clinic.name}</h1>
                            <h2 className="listDetail1 listMargin">Location: {clinic.city} {clinic.street} str.</h2>
                            <h3 className="listDetail2 listMargin">{clinic.clinicType}</h3>
                            <h3 className="listDetail3 listMargin">Open: {clinic.openingHours}</h3>
                        </div>
                    );
                })}
        </div>
    );
};
