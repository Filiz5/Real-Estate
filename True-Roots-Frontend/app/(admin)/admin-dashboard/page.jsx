import { fetchStatistics } from "@/services/report-service";
import StatisticsGrid from "../../../components/admin/admin-statistics/StatisticsGrid"
import { wait } from "@/utils/wait";

  // Sayfanın dinamik olduğunu belirtiyoruz
 export const dynamic = "force-dynamic";

export default async function AdminDashboard() {


  // Veriyi sunucu tarafında çekiyoruz
  const data = await fetchStatistics();
  return (
    <div>
      {/* Veriyi Client Component'e prop olarak geçiriyoruz */}
      <StatisticsGrid statistics={data.object} />
    </div>
  );
}