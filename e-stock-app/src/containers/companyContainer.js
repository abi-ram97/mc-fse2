import { Table, Button, Dropdown, DropdownButton } from 'react-bootstrap';
import { useEffect, useState } from 'react';
import Company from '../components/company';
import { getCompany, getAllCompany } from '../services/companyService';

function CompanyContainer(props) {
    const { setCompanyCode } = props;
    const [companies, setCompanies] = useState([]);
    const [companyDetails, setCompanyDetails] = useState({});
    const [companySearch, setCompanySearch] = useState();

    useEffect(() => {
        let mounted = true;
        getAllCompany()
            .then(items => {
                if (mounted) {
                    console.log("effect call=>" + items)
                    setCompanies(items.body)
                }
            })
        return () => mounted = false;
    }, [])
    const handleSubmit = () => {
        if (companySearch !== "") {
            getCompanyByCode(companySearch);
        } else {
            alert("Enter Company Name to Search")
        }

    }
    const handleChange = (event) => {
        if (event.target && event.target.name === "company") {
            setCompanySearch(event.target.value)
        }
        else {
            getCompanyByCode(event);
        }

    }

    const getCompanyByCode = (code) => {
        getCompany(code).then(res => {
            if (res && res.body) {
                setCompanyCode(res.body)
                setCompanyDetails(res.body)
            } else {
                setCompanyCode({})
                setCompanyDetails({})
                alert('No company found')
            }
        });

    }
    return <div style={{ 'marginTop': '100px', 'width': '75%' }}>
        <Table borderless={true} size="sm"><tbody><tr>
            <td><DropdownButton
                alignRight
                title="Companies"
                size="sm"
                id="dropdown-menu-align-right"
                onSelect={handleChange}>
                {companies ? companies.map(company =>
                    <Dropdown.Item name='dropdown' key={company.companyCode} eventKey={company.companyCode}>
                        {company.companyName}</Dropdown.Item>)
                :''}
            </DropdownButton></td>
            <td><input type="textbox" placeholder="Enter Company Code" name='company' onChange={handleChange}>
            </input><Button style={{
                'marginLeft': '5px',
                'marginBottom': '3px'
            }} size="sm" variant="primary" type="submit" name='submit' onClick={handleSubmit} >Search</Button></td>
            </tr></tbody></Table>
        {Object.keys(companyDetails).length > 0 ?
            <div>
                <Company companyCode={companyDetails.companyCode} companyName={companyDetails.companyName} />
            </div> : ''
        }
    </div>



}

export default CompanyContainer;