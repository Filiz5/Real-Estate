import * as yup from 'yup';  
import React, { useState, useEffect, useMemo } from "react";  
import { Form, Button, Container, Row, Col } from "react-bootstrap";  
import { useRouter } from "next/navigation";  
import Swal from "sweetalert2";  
import { updateUser, deleteUser } from "@/actions/admin-user";  
import { fetchUser } from "@/services/admin-user-service";  
import {FaRegArrowAltCircleLeft, FaSave, FaTrash } from "react-icons/fa";  
import { userSchema } from "../../../helpers/schemas/admin-user-edit";    
import "@/styles/components/admin/admin-user/admin-user-edit.scss";  
import AdvertList from "./admin-advert-list";  
import FavoritesList from "./admin-favorites-list";  
import TourRequestsList from "./admin-toure-request-list";  
import AdminUserLogs from "./AdminUserLogs";  


export const AdminUserEdit = ({ userId }) => {
  const router = useRouter();
  const roles = useMemo(() => ["Admin", "Manager","Customer"], []);
  const [user, setUser] = useState(null);
  const [userRoles, setUserRoles] = useState([]);
  const [error, setError] = useState(null);
  const [formErrors, setFormErrors] = useState({});

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const data = await fetchUser(userId);
        const fetchedRoles = data.object.userRoles || [];

        setUser({
          ...data.object,
          role: fetchedRoles[0] || "",
        });
        setUserRoles(fetchedRoles);
      } catch (error) {
        console.error("Error fetching user data:", error);
        setError(error.message);
      }
    };

    if (userId) {
      fetchUserData();
    }
  }, [userId]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "role") {
      setUserRoles([value]);
    } else {
      setUser((prevUser) => ({
        ...prevUser,
        [name]: value,
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await userSchema.validate(user, { abortEarly: false });

      const updatedUser = {
        ...user,
        userRoles: userRoles,
      };
      await updateUser(userId, updatedUser);
      Swal.fire("Updated!", "User data has been successfully updated.", "success");
    } catch (err) {
      if (err instanceof yup.ValidationError) {
        const validationErrors = {};
        err.inner.forEach((error) => {
          validationErrors[error.path] = error.message;
        });
        setFormErrors(validationErrors);
      } else {
        setError("User could not be updated");
        console.error("Error updating user:", err);
      }
    }
  };

  const handleDelete = async () => {
    const result = await Swal.fire({
      title: "Are you sure?",
      text: "This action cannot be undone!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, delete!",
      cancelButtonText: "Cancel",
    });

    if (result.isConfirmed) {
      try {
        await deleteUser(userId);
        Swal.fire("Deleted!", "User has been successfully deleted.", "success");
      } catch (error) {
        Swal.fire("Error!", "Unable to delete user.", "error");
        console.error("Error deleting user:", error);
      }
    }
  };

  if (error) return <p>Error: {error}</p>;
  if (!user) return <p>Loading...</p>;

  return (
    <Container fluid="true">
      <Row className="justify-content-center mt-5">
        <Col md={8} lg={6}>
          <Form onSubmit={handleSubmit}>
            <Row className="mb-3">
              <Col xs={12} md={6}>
                <Form.Group>
                  <Form.Label><strong>First Name</strong></Form.Label>
                  <Form.Control
                    type="text"
                    name="firstName"
                    value={user.firstName || ""}
                    onChange={handleChange}
                    className="form-input"
                    isInvalid={!!formErrors.firstName}
                    disabled={true} // Disabled
                  />
                  <Form.Control.Feedback type="invalid">{formErrors.firstName}</Form.Control.Feedback>
                </Form.Group>
              </Col>
              <Col xs={12} md={6}>
                <Form.Group>
                  <Form.Label><strong>Last Name</strong></Form.Label>
                  <Form.Control
                    type="text"
                    name="lastName"
                    value={user.lastName || ""}
                    onChange={handleChange}
                    className="form-input"
                    isInvalid={!!formErrors.lastName}
                    disabled={true} // Disabled
                  />
                  <Form.Control.Feedback type="invalid">{formErrors.lastName}</Form.Control.Feedback>
                </Form.Group>
              </Col>
            </Row>

            <Row className="mb-3">
              <Col xs={12} md={4}>
                <Form.Group>
                  <Form.Label><strong>Phone</strong></Form.Label>
                  <Form.Control
                    type="text"
                    name="phone"
                    value={user.phone || ""}
                    onChange={handleChange}
                    className="form-input"
                    isInvalid={!!formErrors.phone}
                    disabled={true} // Disabled
                  />
                  <Form.Control.Feedback type="invalid">{formErrors.phone}</Form.Control.Feedback>
                </Form.Group>
              </Col>
              <Col xs={12} md={5}>
                <Form.Group>
                  <Form.Label><strong>Email</strong></Form.Label>
                  <Form.Control
                    type="email"
                    name="email"
                    value={user.email || ""}
                    onChange={handleChange}
                    className="form-input"
                    isInvalid={!!formErrors.email}
                    disabled={true} // Disabled
                  />
                  <Form.Control.Feedback type="invalid">{formErrors.email}</Form.Control.Feedback>
                </Form.Group>
              </Col>
              <Col xs={12} md={3}>
                <Form.Group>
                  <Form.Label><strong>Role</strong></Form.Label>
                  <Form.Select
                    name="role"
                    value={userRoles[0] || ""}
                    onChange={handleChange}
                    className="form-input"
                    isInvalid={!!formErrors.role}
                    disabled={false} // Only this field is editable
                  >
                    {roles.map((role, index) => (
                      <option key={index} value={role}>
                        {role}
                      </option>
                    ))}
                  </Form.Select>
                  <Form.Control.Feedback type="invalid">{formErrors.role}</Form.Control.Feedback>
                </Form.Group>
              </Col>
            </Row>

            <Row className="mb-3 text-end">
              <Col xs={12}>
              <Button variant='link' className='action-button no-background p-2'>
                  <FaRegArrowAltCircleLeft onClick={() => router.back()} className="icon-color" />
                </Button>
                <Button variant="link" type="submit" className="action-button no-background">
                  <FaSave className="icon-color" />
                </Button>
                <Button variant="link" onClick={handleDelete} className="ms-2 action-button no-background">
                  <FaTrash className="icon-color" />
                </Button>
                
              </Col>
            </Row>
          </Form>
        </Col>
      </Row>
      <Row>
        <Col>
          <h3 className="section-title"><span>User Adverts</span></h3>
          <AdvertList userAdverts={user?.adverts || []} />
        </Col>
      </Row>
      <Row>
        <Col>
          <h3 className="section-title"><span>User Tour Requests</span></h3>
          <TourRequestsList userTourRequests={user?.tourRequests || []} />
        </Col>
      </Row>
      <Row>
        <Col>
          <h3 className="section-title"><span>User Favorites</span></h3>
          <FavoritesList userFavorites={user?.favorites || []} />
        </Col>
      </Row>
      <Row>
        <Col>
          <h3 className="section-title"><span>User Logs</span></h3>
          <AdminUserLogs userLogs={user?.logs || []} />
        </Col>
      </Row>
    </Container>
  );
};