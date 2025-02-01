import React, { useState, useMemo } from "react";
import Swal from "sweetalert2";
import { Container, Pagination, Button } from "react-bootstrap";
import { FaEdit, FaTrash } from "react-icons/fa";
import { deleteTourRequest } from "@/actions/admin-user";
import Image from "next/image";
import { useRouter } from 'next/navigation';

const TourRequestsList = ({ userTourRequests }) => {
  const router = useRouter();
  const [requests, setRequests] = useState(userTourRequests);
  const [page, setPage] = useState(1);
  const [pageSize] = useState(5);
 

  const paginatedTourRequests = useMemo(() => {
    return requests.slice((page - 1) * pageSize, page * pageSize);
  }, [requests, page, pageSize]);

  const totalPages = Math.ceil(requests.length / pageSize);

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  const handleDeleteRequest = async (id) => {
    const result = await Swal.fire({
      title: "Are you sure?",
      text: "Are you sure you want to delete this tour request?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Yes, delete it!",
      cancelButtonText: "No, keep it",
    });

    if (result.isConfirmed) {
      try {
        await deleteTourRequest(id);
        Swal.fire("Deleted!", "Tour request has been deleted successfully.", "success");

               setRequests((prevRequests) => prevRequests.filter(request => request.id !== id));

      } catch (error) {
        console.error('Error occurred while deleting the tour request:', error);
        Swal.fire("Error!", "An error occurred while deleting the tour request.", "error");
      }
    }
  };

  const handleRequestDetails = (id) => {
    router.push(`/admin-dashboard/admin-tour-requests/${id}`);
  };


  return (
    <Container fluid="true" className="mt-5">
      <table className="table table-striped">
        <thead className="thead-light">
          <tr>
            <th>Property</th>
            <th>Tour Date</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {requests.length === 0 ? (
            <tr>
              <td colSpan="4" className="text-center">
                No tour requests available.
              </td>
            </tr>
          ) : (
            paginatedTourRequests.map((request) => (
              <tr key={request.id}>
                <td>
                  <div className="d-flex align-items-center">
                    <Image
                      src={request.advert.images?.[0]?.data || "/default-image.jpg"}
                      alt={request.advert.title || "Advert Image"} 
                      className="advert-image"
                      width={500}
                      height={300}
                    />
                    <div className="ms-3">
                      <p>{request.advert.title}</p>
                      <p>{request.advert.description}</p>
                      <p>{request.advert.location}</p>
                      <p>${request.advert.price}</p>
                    </div>
                  </div>
                </td>
                <td>
                  {new Date(request.tourDate).toLocaleDateString()}{" "}
                  {new Date(request.tourDate).toLocaleTimeString()}
                </td>
                <td>
                  <span className="badge bg-primary">{request.tourRequestStatus}</span>
                </td>
                <td>
                  <Button
                    variant="link"
                    onClick={() => handleDeleteRequest(request.id)}
                    className="btn btn-link action-button gap-2"
                  >
                    <FaTrash />
                  </Button>
                  <Button
                    variant="link"
                    onClick={() => handleRequestDetails(request.id)}
                    className="btn btn-link action-button gap-2"
                  >
                    <FaEdit />
                  </Button>

                
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
      <div className="d-flex justify-content-end mt-4">
        <Pagination className="custom-pagination">
          <Pagination.First onClick={() => handlePageChange(1)} disabled={page === 1} />
          <Pagination.Prev onClick={() => handlePageChange(page - 1)} disabled={page === 1} />
          <Pagination.Item>{page} / {totalPages}</Pagination.Item>
          <Pagination.Next onClick={() => handlePageChange(page + 1)} disabled={page === totalPages} />
          <Pagination.Last onClick={() => handlePageChange(totalPages)} disabled={page === totalPages} />
        </Pagination>
      </div>
    </Container>
  );
};

export default TourRequestsList;