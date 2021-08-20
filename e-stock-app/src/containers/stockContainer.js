import { Table, Button } from 'react-bootstrap';
import { useEffect, useState } from 'react';
import { getStockByInterval } from '../services/stockService';
import Stock from '../components/stock';
import DatePicker from "react-datepicker";
import moment from 'moment'
import "react-datepicker/dist/react-datepicker.css";


const StockContainer = (props) => {
    const { company } = props;

    const [stocks, setStocks] = useState([]);
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [prices, setPrices] = useState({});

    useEffect(() => {
        if (company && company.stocks ) {
            let prices = company.stocks.filter(stock => stock != null);
            setStocks(prices)
            updatePrices(prices)
        } else {
            setStocks([])
            setPrices({})
        }
        return;
    }, [company])

    const updatePrices = (stocks) => {
        let arr = stocks.map(stock => (stock.stockPrice));
        if (arr && arr.length > 0) {
            let min = Math.min(...arr);
            let max = Math.max(...arr);
            let avg = (min + max) / 2;
            setPrices({ min: min, max: max, avg: avg })
        }
    }
    const handleSubmit = () => {
        if (startDate !== "" && endDate !== "" && company.companyCode !== "") {
            const criteria = {
                startDate: moment(startDate).format("yyyy-MM-DD"),
                endDate: moment(endDate).format("yyyy-MM-DD")
            };
            getStockByInterval(company.companyCode, criteria)
                .then(items => {
                    console.log("items=>" + items)
                    setStocks(items.body)
                    updatePrices(items.body)
                })
        } else {
            alert("Enter Valid Start and End Date");
        }
    }
    return (
        <div style={{ 'width': '75%' }}>
            {stocks &&
                <div>
                    <Table borderless={true} size="sm">
                        <tbody><tr>
                        <td><label>Start Date:</label></td><td><DatePicker size="sm"
                            placeholderText="Select Start Date" dateFromat='YYYY-MM-dd'
                            selected={startDate} onChange={(date) => setStartDate(date)} maxDate={new Date()} /> </td>
                        <td><label>End Date:</label></td><td><DatePicker size="sm"
                            placeholderText="Select End Date" dateFromat='YYYY-MM-dd'
                            selected={endDate} onChange={(date) => setEndDate(date)} maxDate={new Date()}/> </td>
                        <td><Button size="sm" variant="primary" type="submit" name='submit'
                            onClick={handleSubmit} >Submit</Button></td>
                    </tr></tbody>
                    </Table>
                </div>}
            {stocks &&
                <div>
                    <Table variant="dark" striped bordered hover size="sm"><thead><tr>
                        <th>Stock Price</th>
                        <th>Date</th>
                        <th>Time</th>
                    </tr></thead><tbody>
                            {stocks && stocks.map(stock => <Stock key={stock.stockId} stockPrice={stock.stockPrice}
                                date={stock.date} time={stock.time} />)}
                            {Object.keys(stocks).length === 0 ? <tr colSpan={3}><td colSpan={3}>No Record Found</td></tr> : ''}
                        </tbody></Table>
                    {stocks && stocks.length > 0 && prices ?
                        <div style={{ 'marginBottom': '100px' }}>
                            <p>Min:{prices.min}</p>
                            <p>Max:{prices.max}</p>
                            <p>Avg:{prices.avg}</p>
                        </div> : ''}
                </div>

            }
        </div>
    );
}

export default StockContainer;