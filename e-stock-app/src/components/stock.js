const Stock = (props) => {
    const { stockPrice, date, time} = props;
    return (
        <tr>
            <td>{stockPrice}</td>
            <td>{date}</td>
            <td>{time}</td>
        </tr>
    );
}

export default Stock