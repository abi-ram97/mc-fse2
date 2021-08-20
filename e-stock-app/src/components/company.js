import { Table } from "react-bootstrap";

const Company = (props) => {
    const {companyCode, companyName} = props;
    return(
        <Table borderless={true} size="sm"><tr>
        <td><label>Company Code:</label></td><td>{companyCode}</td></tr>
      <tr><td><label>Company Name:</label></td><td>{companyName}</td>
    </tr>
    </Table>
    );
}

export default Company