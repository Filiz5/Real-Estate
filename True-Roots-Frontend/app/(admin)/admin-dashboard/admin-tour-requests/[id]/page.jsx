import DetailsTourRequests from '@/components/admin/admin-tour-request/tour-request-details'
import { getTourRequestById } from '@/services/admin-tour-request';

export default async function TourRequestsDetails({ params }) {
  const data = await getTourRequestById(params.id);

  return <DetailsTourRequests data={data} />;
  
}
