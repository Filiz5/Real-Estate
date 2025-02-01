import React, { useState, useMemo } from 'react';
import { Container, Pagination } from 'react-bootstrap';

const AdminUserLogs = ({ userLogs }) => {
  const [currentPage, setCurrentPage] = useState(1);
  const logsPerPage = 4;

  const paginatedLogs = useMemo(() => {
    const indexOfLastLog = currentPage * logsPerPage;
    const indexOfFirstLog = indexOfLastLog - logsPerPage;
    return userLogs.slice(indexOfFirstLog, indexOfLastLog);
  }, [userLogs, currentPage, logsPerPage]);

  const totalPages = Math.ceil(userLogs.length / logsPerPage);

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  return (
    <Container fluid="true" className="mt-5">
      <table className="table table-striped">
        <thead className="thead-light">
          <tr>
            <th>Action</th>
            <th>Date</th>
          </tr>
        </thead>
        <tbody>
          {paginatedLogs.map((log) => (
            <tr key={log.id}>
              <td>{log.log}</td>
              <td>{new Date(log.createdAt).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="d-flex justify-content-end mt-4">
        <Pagination className="custom-pagination">
          <Pagination.First onClick={() => handlePageChange(1)} disabled={currentPage === 1} />
          <Pagination.Prev onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1} />
          <Pagination.Item>{currentPage} / {totalPages}</Pagination.Item>
          <Pagination.Next onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages} />
          <Pagination.Last onClick={() => handlePageChange(totalPages)} disabled={currentPage === totalPages} />
        </Pagination>
      </div>
    </Container>
  );
};

export default AdminUserLogs;