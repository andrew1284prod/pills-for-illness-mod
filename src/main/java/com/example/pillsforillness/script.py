import os

def merge_files_to_txt(output_file="merged_output.txt"):
    """
    Объединяет содержимое всех файлов в текущей папке (кроме этого скрипта)
    в один текстовый файл с заданным форматом.

    Args:
        output_file (str): Путь к выходному файлу (по умолчанию "merged_output.txt")
    """
    # Получаем список всех файлов в текущей директории
    all_files = [f for f in os.listdir() if os.path.isfile(f)]

    # Исключаем этот скрипт из списка
    script_name = os.path.basename(__file__)
    input_files = [f for f in all_files if f != script_name]

    if not input_files:
        print("В текущей папке нет файлов для объединения (кроме этого скрипта)")
        return

    with open(output_file, 'w', encoding='utf-8') as outfile:
        for file_path in input_files:
            try:
                # Получаем имя файла без пути
                file_name = os.path.basename(file_path)

                # Открываем и читаем содержимое файла
                with open(file_path, 'r', encoding='utf-8') as infile:
                    content = infile.read()

                # Записываем в выходной файл в требуемом формате
                outfile.write(f"[{file_name}]:\n")
                outfile.write(f"{content}\n\n")
                outfile.write("-" * 50 + "\n\n")  # Разделитель между файлами

                print(f"Успешно обработан файл: {file_name}")
            except Exception as e:
                print(f"Ошибка при обработке файла {file_path}: {str(e)}")

    print(f"\nВсе файлы объединены в {output_file}")

if __name__ == "__main__":
    merge_files_to_txt()
