"use client"

import AdminContactMessageDetails from "@/components/admin/contact-message/AdminContactMessageDetails";
import React, { useState } from "react";
import { Container, Row, Col } from "react-bootstrap";


const Page = ({ params }) => {
  const { id } = params; 
  const [page, setPage] = useState(0); 

  return (
    <Container className="mt-5">
      <Row className="justify-content-center">
        <Col md={8}>
          <AdminContactMessageDetails id={id} page={page} />
        </Col>
      </Row>
    </Container>
  );
};

export default Page;