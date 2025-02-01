"use client";

import React, { useState, useEffect } from "react";
import { Card, Button, Container, Row, Col, Pagination, Form, InputGroup } from "react-bootstrap";
import { fetchUsers } from "@/services/admin-user-service";  
import { deleteUser } from "@/actions/admin-user";
import "bootstrap/dist/css/bootstrap.min.css";
import { FaEdit, FaTrash, FaSearch } from "react-icons/fa";
import { useRouter } from "next/navigation"; 
import Swal from "sweetalert2";

const AdminUser = () => {
  const [users, setUsers] = useState([]);
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [searchQuery, setSearchQuery] = useState(""); 
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1); 
  const [error, setError] = useState(null);
  const router = useRouter(); 

  useEffect(() => {
    const getUsers = async () => {
      try {
        const data = await fetchUsers(page);
        setUsers(data.content); 
        setTotalPages(data.totalPages); 
      } catch (error) {
        setError(error.message);
      }
    };

    getUsers();
  }, [page]);

  useEffect(() => {
    if (searchQuery === "") {
      setFilteredUsers(users); 
    } else {
      setFilteredUsers(
        users.filter(user => 
          `${user.firstName} ${user.lastName}`.toLowerCase().includes(searchQuery.toLowerCase())
        )
      );
    }
  }, [searchQuery, users]);

  const handlePaginationChange = (newPage) => {
    setPage(newPage); 
  };

  const handleDelete = async (userId) => {
    Swal.fire({
      title: 'Are you sure?',
      text: "You won't be able to revert this!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!'
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          await deleteUser(userId);

          setUsers(users.filter(user => user.userId !== userId));
          setFilteredUsers(filteredUsers.filter(user => user.userId !== userId));

          Swal.fire(
            'Deleted!',
            'The user has been deleted.',
            'success'
          );
        } catch (error) {
          setError(`Failed to delete user: ${error.message}`);
        }
      }
    });
  };

  const handleEdit = (userId) => {
    router.push(`/admin-dashboard/admin-users/${userId}`);
  };

  return (
    <Container>
      {error && <p>Error: {error}</p>}

      <Row className="mb-4">
        <Col xs={12} md={6}>
          <InputGroup>
            <InputGroup.Text>
              <FaSearch />
            </InputGroup.Text>
            <Form.Control
              type="text"
              placeholder="Search users..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </InputGroup>
        </Col>
      </Row>
    
      <Row className="g-4">
        {filteredUsers.map((user, index) => (
          <Col 
            key={user.userId} 
            xs={12} 
            className="mb-3"
          >
            <Card className="w-100" style={{ backgroundColor: "#fff2f2" }}>
              <Card.Body>
                <Row className="d-flex align-items-center justify-content-center">
                  <Col xs={12} md={1} className="text-center">
                    <strong>{index + 1}</strong> 
                  </Col>
                  
                  <Col xs={12} md={11}>
                    <Row className="d-flex justify-content-center">
                      <Col xs={12} md={4} className="text-start">
                        <Card.Title>
                          {user.firstName} {user.lastName}
                        </Card.Title>
                      </Col>

                      <Col xs={12} md={4} className="text-start">
                        <Card.Text>
                          <strong>Email:</strong> {user.email}
                        </Card.Text>
                      </Col>

                      <Col xs={12} md={4} className="d-flex justify-content-between align-items-center">
                        <Card.Text className="text-start">
                          <strong>Phone:</strong> {user.phone}
                        </Card.Text>
                        <div className="d-flex">
                          <Button 
                            variant="link" 
                            className="text-primary"
                            onClick={() => handleEdit(user.userId)} 
                          >
                            <FaEdit size={20} />
                          </Button>
                          <Button 
                            variant="link" 
                            className="text-danger ms-3" 
                            onClick={() => handleDelete(user.userId)}
                          >
                            <FaTrash size={20} />
                          </Button>
                        </div>
                      </Col>
                    </Row>
                  </Col>
                </Row>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      <div className="d-flex justify-content-end mt-4">
        <Pagination className="custom-pagination">
          <Pagination.First onClick={() => handlePaginationChange(0)} disabled={page === 0} />
          <Pagination.Prev onClick={() => handlePaginationChange(page - 1)} disabled={page === 0} />
          <Pagination.Item>{page + 1} / {totalPages}</Pagination.Item>
          <Pagination.Next onClick={() => handlePaginationChange(page + 1)} disabled={page === totalPages - 1} />
          <Pagination.Last onClick={() => handlePaginationChange(totalPages - 1)} disabled={page === totalPages - 1} />
        </Pagination>
      </div>
    </Container>
  );
};

export default AdminUser;