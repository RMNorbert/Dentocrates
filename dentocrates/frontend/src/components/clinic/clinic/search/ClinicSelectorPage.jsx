import "./ClinicSelectorPage.css";
import { useEffect, useState } from "react"
import { ClinicList } from "./ClinicList"
import { Loading } from "../../../lodaingPage/Loading";
import { MultiFetch } from "../../../../utils/fetch/MultiFetch";
export const ClinicSelectorPage = () => {
    const { data } = MultiFetch();
    const [isDataLoaded, setIsDataLoaded] = useState(false);
    const [clinicData, setClinicData] = useState([]);
    const [filteredData, setFilteredData] = useState([]);

    const getClinicData = async () => {
        const clinicDataUrl = `/clinic/all`;
        const responseData = await data(clinicDataUrl);
        setClinicData(await responseData);
        setFilteredData(await responseData);
        setIsDataLoaded(true);
    };


    const search = (event) => {
        const searchText = event.target.value;
        const filteredClinics = clinicData.filter((clinic) =>
            clinic.name.toLowerCase().includes(searchText.toLowerCase())
        );
        setFilteredData(filteredClinics);
    };

    useEffect(() => {
        if(!isDataLoaded) {
            getClinicData();
        }
        }, [filteredData]);

    if (isDataLoaded) {
        return (
            <div className="selector">
                <div>
                    <input
                        className={"searchBar"}
                        type={"text"}
                        placeholder={"Search for clinics"}
                        onChange={(event) => search(event)}
                    />
                </div>
                <div className="list">
                    {filteredData.length > 0 ? (
                <ClinicList clinicDatas={filteredData} />
                        ) :
                        ( <p>No results found.</p>)
                    }
                </div>
            </div>
        );
    } else {
        return <Loading />;
    }
};
