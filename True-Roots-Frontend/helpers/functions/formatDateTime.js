export function formatDateTime(input) {
  if (input.includes('T')) {
    // Handle ISO Date-Time string (e.g., 2025-12-29T14:35:00)
    const date = new Date(input);

    // Format the date as MM/DD/YYYY
    const formattedDate = `${(date.getMonth() + 1)
      .toString()
      .padStart(2, '0')}/${date
      .getDate()
      .toString()
      .padStart(2, '0')}/${date.getFullYear()}`;

    // Format the time as hh:mm AM/PM
    const hours = date.getHours();
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const ampm = hours >= 12 ? 'PM' : 'AM';
    const formattedTime = `${(hours % 12 || 12)
      .toString()
      .padStart(2, '0')}:${minutes} ${ampm}`;

    return { formattedDate, formattedTime };
  } else {
    // Handle time string only (e.g., 22:45)
    const [hours, minutes] = input.split(':').map(Number);
    const ampm = hours >= 12 ? 'PM' : 'AM';
    const formattedTime = `${(hours % 12 || 12)
      .toString()
      .padStart(2, '0')}:${minutes.toString().padStart(2, '0')} ${ampm}`;

    return { formattedTime };
  }
}
