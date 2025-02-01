import React, { useState, useEffect } from "react";  
import { Button, Card, Col, Row, Container } from "react-bootstrap";  
import { useRouter } from "next/navigation";  
import Swal from "sweetalert2";  
import styles from "../../../styles/components/admin/admin-contact-message/admin-contact-messages-details.module.scss";  

import {  
  deleteContactMessage,  
  updateMessageReadStatus,  
  getContactMessagesFromDatabase,  
} from "@/services/contact";  
import Loader from "@/components/common/Loader";

const AdminContactMessageDetails = ({ id, page }) => {  
  const [message, setMessage] = useState(null);  
  const [loading, setLoading] = useState(true);  
  const [error, setError] = useState(null);  
  const router = useRouter();  

  useEffect(() => {  
    const fetchMessages = async () => {  
      try {  
        setLoading(true);  
        const { content } = await getContactMessagesFromDatabase(page);  
        const foundMessage = content.find((msg) => msg.id === parseInt(id));  

        if (foundMessage) {  
          setMessage(foundMessage);  

          if (!foundMessage.read) {  
            await updateMessageReadStatus(id, true);  
            foundMessage.read = true;  
          }  
        } else {  
          setError("Message not found");  
        }  
      } catch (error) {  
        setError("An error occurred while fetching the message.");  
      } finally {  
        setLoading(false);  
      }  
    };  

    fetchMessages();  
  }, [id, page]);  

  const handleDelete = async () => {  
    Swal.fire({  
      title: "Are you sure?",  
      text: "Once deleted, you will not be able to recover this message!",  
      icon: "warning",  
      showCancelButton: true,  
      confirmButtonText: "Yes, delete it!",  
      cancelButtonText: "Cancel",  
    }).then(async (result) => {  
      if (result.isConfirmed) {  
        try {  
          await deleteContactMessage(id);  
          Swal.fire({  
            icon: "success",  
            title: "Deleted!",  
            text: "The message has been successfully deleted.",  
          });  
          router.push("/admin-dashboard/admin-contact-messages");  
        } catch (error) {  
          Swal.fire({  
            icon: "error",  
            title: "Oops...",  
            text: "Failed to delete the message. Please try again.",  
          });  
        }  
      }  
    });  
  };  

  if (loading)  
    return (  
     <Loader/> 
    );  

  if (error) return <div className="alert alert-danger">{error}</div>;  

  if (!message) return <div>Message not found</div>;  

  return (  
    <Container fluid className="px-0">  
      <Card className={styles.messageContainer}>  
        <Card.Header as="h5" className="text-center">  
          Message Details  
        </Card.Header>  
        <Card.Body>  
          <Row className="mb-3">  
            <Col xs={12} sm={4}>  
              <strong>Name:</strong>  
            </Col>  
            <Col xs={12} sm={8}>  
              {message.firstName} {message.lastName}  
            </Col>  
          </Row>  
          <Row className="mb-3">  
            <Col xs={12} sm={4}>  
              <strong>Email:</strong>  
            </Col>  
            <Col xs={12} sm={8}>{message.email}</Col>  
          </Row>  
          <Row className="mb-3">  
            <Col xs={12} sm={4}>  
              <strong>Message:</strong>  
            </Col>  
            <Col xs={12} sm={8}>  
              <div className={styles.messageContent}>  
                {message.message}  
              </div>  
            </Col>  
          </Row>  
          <Row className="mt-4">  
            <Col xs={12} className="d-flex justify-content-end gap-2">  
              <Button  
                variant="primary"  
                onClick={() => router.push("/admin-dashboard/admin-contact-messages")}  
              >  
                Return  
              </Button>  
              <Button variant="warning" onClick={handleDelete}>  
                Delete  
              </Button>  
            </Col>  
          </Row>  
        </Card.Body>  
      </Card>  
    </Container>  
  );  
};  

export default AdminContactMessageDetails;  