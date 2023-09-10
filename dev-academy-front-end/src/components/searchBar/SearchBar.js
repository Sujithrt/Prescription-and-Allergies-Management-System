import React, { useState } from 'react';
import './../searchBar/searchBar.css';
import { retrievePatients } from './../../api/PnamsService';
import AsyncSelect from 'react-select/async';
import makeAnimated from 'react-select/animated';
import { useHistory } from 'react-router-dom';
import { customStyles } from '../../Constants';


const animatedComponent = makeAnimated();

/**
 * Renders Search Bar in the Navigation Bar to search for patients
 * 
 */
export default function SearchBar() {    

    const history = useHistory();

    const [selected, setSelected] = useState("");

    /**
     * Loads list of patients from database and displays it to the user to select and navigate to patient profile
     * @param searchText 
     * @returns 
     */
    const loadPatients = async (searchText) => (
        retrievePatients(searchText)
            .then((response) =>
                response.data.map(i => ({ label: i.firstName + " " + i.lastName, value: i.id }))
            )
            .catch((error) => {
                history.push("/error");
            })
    )

    const onChange = (selected) => {
        setSelected(selected);
        if (null !== selected) {
            history.push(`/patient/${selected.value}`);
            history.go();
        }
    }

    return (
        <div className="container">
            <AsyncSelect
                data-testid="patientSearchBar"
                className="search-bar"
                components={animatedComponent}
                value={selected}
                onChange={onChange}
                isClearable={true}
                placeholder="Search Patients..."
                loadOptions={loadPatients}
                styles={customStyles}
            />
        </div>
    )
}
