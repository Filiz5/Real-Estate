"use client";

import React, { useState, useEffect, useCallback, useMemo } from "react";
import { Container, Row, Col, Form, Button, Dropdown, InputGroup, Pagination, Accordion } from "react-bootstrap";
import Link from "next/link";
import "bootstrap/dist/css/bootstrap.min.css";
import "@/styles/components/admin/admin-contact-message/admin-contact-messages.scss";
import { getContactMessagesFromDatabase, deleteContactMessage, updateMessageReadStatus } from "@/services/contact";
import Swal from "sweetalert2";
import {FaTrash, FaEye, FaEnvelopeOpen } from 'react-icons/fa';
import Loader from "@/components/common/Loader";

const formatDate = (dateString) => {
  const date = new Date(dateString);
  return new Intl.DateTimeFormat('en-GB', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false,
  }).format(date);
};

const handleError = (title, text) => {
  Swal.fire({
    icon: "error",
    title,
    text,
  });
};

export const AdminContactMessages = () => {
  const [activePage, setActivePage] = useState(1);
  const [searchTerm, setSearchTerm] = useState("");
  const [filter, setFilter] = useState("All messages");
  const [messages, setMessages] = useState([]);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);
  const [selectedMessages, setSelectedMessages] = useState(new Set());
  const [unreadCount, setUnreadCount] = useState(0);

  const messagesPerPage = 50;

  const fetchMessages = useCallback(async () => {
    setLoading(true);
    try {
      const { content, totalPages } = await getContactMessagesFromDatabase(activePage - 1, messagesPerPage);
      const sortedMessages = content.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
      setMessages(sortedMessages);
      setTotalPages(totalPages);
    } catch (error) {
      console.error("Error fetching data:", error);
      setMessages([]);
      handleError("Error", "Failed to fetch data. Please try again later.");
    } finally {
      setLoading(false);
    }
  }, [activePage]);

  useEffect(() => {
    fetchMessages();
  }, [fetchMessages]);

  const filteredMessages = useMemo(() => {
    return messages.filter((message) => {
      const isReadFilter = filter === "Read" ? message.read : filter === "Unread" ? !message.read : true;
      const isSearchMatch = [
        message.firstName,
        message.lastName,
        message.email,
        message.message
      ].some(field => field.toLowerCase().includes(searchTerm.toLowerCase()));

      return isReadFilter && isSearchMatch;
    });
  }, [filter, messages, searchTerm]);

  useEffect(() => {
    const filtered = filteredMessages;

    if (filter === "Unread") {
      setUnreadCount(filtered.filter((message) => !message.read).length);
    } else if (filter === "Read") {
      setUnreadCount(filtered.filter((message) => message.read).length);
    } else {
      setUnreadCount(filtered.length);
    }
  }, [filter, filteredMessages]);

  const handlePageChange = (pageNumber) => {
    setActivePage(pageNumber);
  };

  const handleReadToggle = async (id) => {
    const message = messages.find((msg) => msg.id === id);
    if (!message) return;

    try {
      await updateMessageReadStatus(id, !message.read);
      const updatedMessages = messages.map((msg) => msg.id === id ? { ...msg, read: !msg.read } : msg);
      setMessages(updatedMessages);
    } catch (error) {
      console.error("Error updating read status:", error);
      handleError("Error", "Failed to update read status. Please try again.");
    }
  };

  const handleSelectMessage = (id) => {
    const updatedSelectedMessages = new Set(selectedMessages);
    if (updatedSelectedMessages.has(id)) {
      updatedSelectedMessages.delete(id);
    } else {
      updatedSelectedMessages.add(id);
    }
    setSelectedMessages(updatedSelectedMessages);
  };

  const handleSelectAll = () => {
    const newSelection = selectedMessages.size === filteredMessages.length
      ? new Set()
      : new Set(filteredMessages.map((message) => message.id));
    setSelectedMessages(newSelection);
  };

  const handleDeleteSelected = async () => {
    if (selectedMessages.size === 0) {
      Swal.fire({
        icon: 'warning',
        title: 'No messages selected',
        text: 'Please select at least one message to delete.',
      });
      return;
    }

    const result = await Swal.fire({
      title: "Are you sure?",
      text: "Do you really want to delete the selected messages?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Yes, delete them!",
      cancelButtonText: "No, cancel",
    });

    if (result.isConfirmed) {
      try {
        for (let id of selectedMessages) await deleteContactMessage(id);
        setSelectedMessages(new Set());
        fetchMessages();
        Swal.fire({
          icon: 'success',
          title: 'Deleted!',
          text: 'Selected messages have been deleted successfully.',
        });
      } catch (error) {
        handleError('Error', 'Error deleting selected messages. Please try again.');
      }
    }
  };

  const handleDeleteMessage = async (id) => {
    const result = await Swal.fire({
      title: "Are you sure?",
      text: "Do you really want to delete this message?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Yes, delete it!",
      cancelButtonText: "No, cancel",
    });

    if (result.isConfirmed) {
      try {
        await deleteContactMessage(id);
        fetchMessages();
        Swal.fire({
          icon: 'success',
          title: 'Deleted!',
          text: 'Message deleted successfully.',
        });
      } catch (error) {
        handleError('Error', 'Error deleting message. Please try again.');
      }
    }
  };

  if (loading) return <Loader/>;

  return (
    <Container>
      <h1>Admin Contact Messages</h1>
      <Row className="mb-3">
        <Col>
          <InputGroup className="search-container">
            <Form.Control
              type="text"
              placeholder="Search messages"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </InputGroup>
        </Col>
        <Col>
          <Dropdown onSelect={setFilter} className="float-end" aria-label="Filter dropdown">
            <Dropdown.Toggle variant="outline-secondary" id="filter-dropdown">
              {filter}
            </Dropdown.Toggle>
            <Dropdown.Menu>
              <Dropdown.Item eventKey="All messages">All messages</Dropdown.Item>
              <Dropdown.Item eventKey="Read">Read</Dropdown.Item>
              <Dropdown.Item eventKey="Unread">Unread</Dropdown.Item>
            </Dropdown.Menu>
          </Dropdown>
        </Col>
      </Row>

      <Row className="mb-3">
        <Col><h5>{filter === "Unread" ? "Unread" : filter} Messages: {unreadCount}</h5></Col>
      </Row>

      <Button
        style={{ backgroundColor: "#fff3f3", borderColor: "#ffc107", color: "#ffc107" }}
        onClick={handleSelectAll}
        className="mb-3"
      >
        {selectedMessages.size === filteredMessages.length ? "Deselect All" : "Select All"}
      </Button>
      <Button
        style={{ backgroundColor: "#fff3f3", borderColor: "#ffc107", color: "#ffc107" }}
        onClick={handleDeleteSelected}
        className="mb-3 ms-2"
      >
        Delete
      </Button>

      <Accordion className="message-list">
  {filteredMessages.map((message) => (
    <Accordion.Item key={message.id} eventKey={String(message.id)}>
      <Accordion.Header
        className={`message-item ${message.read ? "read" : "unread"} ${selectedMessages.has(message.id) ? "selected" : ""}`}
      >
        <Row className="w-100 align-items-center">
          <Col xs={1} className="text-center">
            <Form.Check
              type="checkbox"
              checked={selectedMessages.has(message.id)}
              onChange={(e) => {
                e.stopPropagation();
                handleSelectMessage(message.id);
              }}
            />
          </Col>
          <Col xs={1} className="text-center" style={{ gap: "2px", display: "flex", justifyContent: "center" }}>
            {message.read ? <FaEnvelopeOpen className="text-primary" /> : <FaEye className="text-warning" />}
          </Col>
          <Col xs={12} md={3}>
            <strong>{message.firstName} {message.lastName}</strong>
          </Col>
          <Col xs={12} md={3}>
            <span className="text-muted">{formatDate(message.createdAt)}</span>
          </Col>
          <Col xs={12} md={4}>
            <span className="text-muted">{message.email}</span>
          </Col>
        </Row>
      </Accordion.Header>
      <Accordion.Body>
      <p className="message-content">{message.message.slice(0, 10)}...</p>

        <div className="d-flex justify-content-between">
          <Button
            variant="link"
            className="text-primary"
            onClick={() => handleReadToggle(message.id)}
          >
            <span className="icon d-flex align-items-center justify-content-center" style={{ minWidth: "24px", minHeight: "24px", fontSize: "1.2rem" }}>
              {message.read ? <FaEnvelopeOpen /> : <FaEye />}
            </span>

            Mark as {message.read ? "Unread" : "Read"}
          </Button>

        
          <Link href={`/admin-dashboard/admin-contact-messages/adminContactMessageDetails/${message.id}`}>
                    <Button variant="link" className="text-primary">View Details</Button>
                  </Link>

          <Button
            variant="link"
            className="text-danger"
            onClick={() => handleDeleteMessage(message.id)}
          >
            <FaTrash />
          </Button>
        </div>
      </Accordion.Body>
    </Accordion.Item>
  ))}
</Accordion>

      <Pagination className="justify-content-center mt-3">
  <Pagination.First
    onClick={() => handlePageChange(1)}
    disabled={activePage === 1}
  />
  <Pagination.Prev
    onClick={() => handlePageChange(activePage - 1)}
    disabled={activePage === 1}
  />

  {[...Array(totalPages)].map((_, index) => (
    <Pagination.Item
      key={index}
      active={index + 1 === activePage}
      onClick={() => handlePageChange(index + 1)}
    >
      {index + 1}
    </Pagination.Item>
  ))}

  <Pagination.Next
    onClick={() => handlePageChange(activePage + 1)}
    disabled={activePage === totalPages}
  />
  <Pagination.Last
    onClick={() => handlePageChange(totalPages)}
    disabled={activePage === totalPages}
  />
  
 
      </Pagination>

    </Container>
  );
};

export default AdminContactMessages;