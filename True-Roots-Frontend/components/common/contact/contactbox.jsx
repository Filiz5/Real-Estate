'use client';

import React, { useState } from 'react';
import { submitContactForm } from '../../../actions/contact'; 
import 'bootstrap/dist/css/bootstrap.min.css';
import Swal from 'sweetalert2';
import 'primeicons/primeicons.css';
        
import { contactValidationSchema } from '../../../helpers/schemas/contact';

const ContactBox = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    message: '',
    isHuman: false,
  });

  const [errors, setErrors] = useState({});
  const [messageLength, setMessageLength] = useState(0);
  const maxLength = 500;

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    if (name === 'message' && value.length > maxLength) {
      return;
    }

    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value,
    });

    if (name === 'message') {
      setMessageLength(value.length);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await contactValidationSchema.validate(formData, { abortEarly: false });
      const response = await submitContactForm(formData);
      Swal.fire({
        title: 'Success!',
        text: 'Your message has been sent successfully!',
        icon: 'success',
        confirmButtonText: 'OK'
      });
      setFormData({
        firstName: '',
        lastName: '',
        email: '',
        message: '',
        isHuman: false,
      });
      setErrors({});
      setMessageLength(0);
    } catch (error) {
      if (error.name === 'ValidationError') {
        const formattedErrors = error.inner.reduce((acc, err) => {
          acc[err.path] = err.message;
          return acc;
        }, {});
        setErrors(formattedErrors);
      } else {
        Swal.fire({
          title: 'Error!',
          text: 'Form submission failed.',
          icon: 'error',
          confirmButtonText: 'OK'
        });
      }
    }
  };

  return (
    <div className="contact-card shadow-lg p-3">
      <h2 className="text-center mb-3 text-primary">Contact us</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-2">
          <label htmlFor="firstName" className="form-label">
            First Name:
          </label>
          <input
            type="text"
            id="firstName"
            name="firstName"
            value={formData.firstName}
            onChange={handleChange}
            required
            className="form-control"
            placeholder="Enter your first name"
          />
          {errors.firstName && <div className="text-danger">{errors.firstName}</div>}
        </div>
        <div className="mb-2">
          <label htmlFor="lastName" className="form-label">
            Last Name:
          </label>
          <input
            type="text"
            id="lastName"
            name="lastName"
            value={formData.lastName}
            onChange={handleChange}
            required
            className="form-control"
            placeholder="Enter your last name"
          />
          {errors.lastName && <div className="text-danger">{errors.lastName}</div>}
        </div>
        <div className="mb-2">
          <label htmlFor="email" className="form-label">
            Email:
          </label>
          <input
            type="email"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
            className="form-control"
            placeholder="Enter your email address"
          />
          {errors.email && <div className="text-danger">{errors.email}</div>}
        </div>
        <div className="mb-2">
          <label htmlFor="message" className="form-label">
            Your Message:
          </label>
          <textarea
            id="message"
            name="message"
            value={formData.message}
            onChange={handleChange}
            required
            className="form-control"
            rows="3"
            placeholder="Write your message here"
          ></textarea>
          {errors.message && <div className="text-danger">{errors.message}</div>}
          <div className="text-muted">Remaining characters: {maxLength - messageLength}</div>
        </div>
        <div className="form-check mb-2 modern-checkbox">
          <input
            type="checkbox"
            id="isHuman"
            name="isHuman"
            checked={formData.isHuman}
            onChange={handleChange}
            className="form-check-input"
          />
          <label htmlFor="isHuman" className="form-check-label">
            <span className="pi pi-check-circle"></span> I am not a robot
          </label>
          {errors.isHuman && <div className="text-danger">{errors.isHuman}</div>}
        </div>
        <button type="submit" className="btn btn-primary w-100">
          <i className="pi pi-send"></i> Send Message
        </button>
      </form>
    </div>
  );
};

export default ContactBox;
