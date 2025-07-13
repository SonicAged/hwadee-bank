/**
 * 格式化日期时间
 * @param dateStr 日期字符串
 * @param format 格式化模板，默认为 'YYYY-MM-DD HH:mm:ss'
 * @returns 格式化后的日期字符串
 */
export function formatDateTime(dateStr: string | null | undefined, format: string = 'YYYY-MM-DD HH:mm:ss'): string {
  if (!dateStr) return '-';
  
  const date = new Date(dateStr);
  if (isNaN(date.getTime())) return '-';
  
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  
  return format
    .replace('YYYY', String(year))
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds);
}

/**
 * 将日期对象转换为后端需要的格式
 * @param date 日期对象或字符串
 * @returns 格式化后的日期字符串，适合后端使用
 */
export function formatDateForBackend(date: Date | string | null | undefined): string {
  if (!date) return '';
  
  // 如果已经是字符串且符合格式，直接返回
  if (typeof date === 'string') {
    if (date.includes('T') && date.length >= 19) {
      return date.substring(0, 19); // 截取前19个字符
    }
  }
  
  // 转换为Date对象
  const dateObj = typeof date === 'string' ? new Date(date) : date;
  if (isNaN(dateObj.getTime())) return '';
  
  // 格式化为 yyyy-MM-dd HH:mm:ss，注意这里使用空格而不是T
  const year = dateObj.getFullYear();
  const month = String(dateObj.getMonth() + 1).padStart(2, '0');
  const day = String(dateObj.getDate()).padStart(2, '0');
  const hours = String(dateObj.getHours()).padStart(2, '0');
  const minutes = String(dateObj.getMinutes()).padStart(2, '0');
  const seconds = String(dateObj.getSeconds()).padStart(2, '0');
  
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

/**
 * 格式化日期
 * @param dateStr 日期字符串
 * @param format 格式化模板，默认为 'YYYY-MM-DD'
 * @returns 格式化后的日期字符串
 */
export function formatDate(dateStr: string | null | undefined, format: string = 'YYYY-MM-DD'): string {
  return formatDateTime(dateStr, format);
}

/**
 * 格式化货币
 * @param value 货币值
 * @param currency 货币符号，默认为 '¥'
 * @param decimals 小数位数，默认为 2
 * @returns 格式化后的货币字符串
 */
export function formatCurrency(value: number | string | null | undefined, currency: string = '¥', decimals: number = 2): string {
  if (value === null || value === undefined) return '-';
  
  const numValue = typeof value === 'string' ? parseFloat(value) : value;
  if (isNaN(numValue)) return '-';
  
  return `${currency}${numValue.toFixed(decimals)}`;
}

/**
 * 格式化文件大小
 * @param size 文件大小（字节）
 * @returns 格式化后的文件大小字符串
 */
export function formatFileSize(size: number | null | undefined): string {
  if (size === null || size === undefined) return '-';
  
  if (size < 1024) {
    return `${size} B`;
  } else if (size < 1024 * 1024) {
    return `${(size / 1024).toFixed(2)} KB`;
  } else if (size < 1024 * 1024 * 1024) {
    return `${(size / (1024 * 1024)).toFixed(2)} MB`;
  } else {
    return `${(size / (1024 * 1024 * 1024)).toFixed(2)} GB`;
  }
} 