import CompanyContiner from '../containers/companyContainer';
import Header from '../components/header';
import StockContainer from './stockContainer';
import Footer from '../components/footer';
import { Container, Row } from 'react-bootstrap';
import { useState } from 'react';

const ViewContainer = () => {
    const [companyCode, setCompanyCode] = useState();
    return <div> <Container>
        <Row className="row-header"><Header></Header></Row>

        <Row className="row-content justify-content-md-center">
            <CompanyContiner setCompanyCode={setCompanyCode} />
        </Row>
        <Row className="row-content justify-content-md-center">
            <StockContainer company={companyCode} />
        </Row>
        <Row className="row-footer"><Footer></Footer></Row>
    </Container></div >;

}

export default ViewContainer;