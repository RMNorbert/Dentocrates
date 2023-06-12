import "./ClinicSelectorPage.css";
import { useEffect, useState } from "react"
import { ClinicList } from "./ClinicList"
import { Loading } from "../elements/Loading";
export const ClinicSelectorPage = () => {
    const [data , setData] = useState();
    const [filteredData, setFilteredData] = useState([]);

    const getClinicData = async() =>{
        const responseData = await fetch(`/clinic/all`);
        setData(await responseData.json());
    }

    const isDataLoaded = () => {
        console.log(!!data);
        return !!data;
    };

    const search = (event) => {
        const searchText = event.target.value;
        const results = [];
        setFilteredData(results);
        //  results.sort((a, b) => b[0] - a[0]);
        //   event.target.value = clinic.name;
    };

    useEffect(() => {
        getClinicData();
    },[data]);
    if(isDataLoaded()){
        return (
            <div className="clinic">
                <div>
                    <input className={"searchBar"} type={"text"} placeholder={"Search for clinics"} onKeyDown={(event) => (event.code === 'Enter') && search(event)}/>
                </div>

                <ClinicList
                    clinicData={data}
                />
            </div>
        )} else {
        return <Loading/>
    }
}
