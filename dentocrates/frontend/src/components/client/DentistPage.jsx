import {useEffect, useState} from "react"
import {Loading} from "../elements/Loading";
import {MultiFetch} from "../../fetch/MultiFetch";
import {useNavigate} from "react-router-dom";

export const DentistPage = () => {
    const { data } = MultiFetch();
    const navigate = useNavigate();
    const [isDataLoaded, setIsDataLoaded] = useState(false);
    const [dentistData, setDentistData] = useState([]);
    const [filteredData, setFilteredData] = useState([]);
    const [clinicData, setClinicData] = useState([]);
    const getDentistDetails = async() =>{
        await getClinicData();
        const responseData = await data(`/java-backend/dentist//all`);
        setDentistData(await responseData);
        setFilteredData(await responseData);
        setIsDataLoaded(true);
    }
    const getClinicData = async () => {
        const responseData = await data(`/java-backend/clinic/all`);
        setClinicData(await responseData);
    }

    const filterClinics = (currentName, currentId) => {
        return clinicData
            .filter((clinic) => clinic.dentistId === currentId)
            .map((relatedClinic) => (
                <div key={relatedClinic.street} onClick={() => navigate(`/clinic/${relatedClinic.id}`)}>
                <h1 className="listName listMargin">{currentName}</h1>
                    <div className="listDetail1 listMargin">
                        {relatedClinic.clinicType.replaceAll("_"," ")}
                    </div>
                    <div className="listDetail2 listMargin">
                        Location: {relatedClinic.city} {relatedClinic.street}
                    </div>
                </div>
            ));
    };
    const search = (event) => {
        const searchText = event.target.value;
        const filteredDentists = dentistData.filter((dentist) =>
            dentist.lastName.toLowerCase().includes(searchText.toLowerCase())
        );
        setFilteredData(filteredDentists);
    };

    useEffect(() => {
        if(!isDataLoaded) {
            getDentistDetails();
        }
    },[filteredData]);

    if (isDataLoaded) {
        return (
            <div className="selector">
                <div>
                    <input
                        className={"searchBar"}
                        type={"text"}
                        placeholder={"Search for dentist"}
                        onChange={(event) => search(event)}
                    />
                </div>
                <div className="list">
                    {filteredData.length > 0 ? (
                        filteredData.map((dentist) => {
                    const name = `Dentist: Dr.${dentist.firstName} ${dentist.lastName}`;
                    return (
                    <div  key={dentist.name}
                          className="listBox"
                    >
                        {filterClinics(name,dentist.id)}
                    </div>
                )})) :
                        ( <p>No results found.</p>)
                    }
                </div>
            </div>
        )
    } else {
        return <Loading />;
    }
}
