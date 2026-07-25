import os
import re

def split_merged_txt(input_file="merged_output.txt"):
    """
    Читает объединенный файл и восстанавливает/перезаписывает
    все содержащиеся в нем файлы.
    """
    if not os.path.exists(input_file):
        print(f"Ошибка: Файл '{input_file}' не найден в текущей папке.")
        return

    try:
        with open(input_file, 'r', encoding='utf-8') as f:
            content = f.read()
    except Exception as e:
        print(f"Не удалось прочитать {input_file}: {e}")
        return

    # Регулярное выражение для поиска блоков файлов.
    # Ищет конструкцию [имя_файла]: ... содержимое ... --------------------------------------------------
    # Флаг re.DOTALL позволяет точке (.) соответствовать символу новой строки.
    pattern = r"\[(.*?)\]:\n(.*?)\n\n--------------------------------------------------\n\n"
    matches = re.findall(pattern, content, re.DOTALL)

    if not matches:
        # Пробуем более мягкий вариант на случай, если последний разделитель был изменен или обрезан
        pattern = r"\[(.*?)\]:\n(.*?)(?=\n\n\[|\n\n--------------------------------------------------|\Z)"
        matches = re.findall(pattern, content, re.DOTALL)

    if not matches:
        print("Не удалось найти файлы в правильном формате внутри переданного файла.")
        return

    script_name = os.path.basename(__file__)

    for file_name, file_content in matches:
        # Очищаем имя файла от возможных пробелов
        file_name = file_name.strip()
        
        # Защита от перезаписи самого себя и файла-источника
        if file_name in (script_name, input_file):
            print(f"Пропуск файла {file_name} (безопасность: совпадает со скриптом или источником)")
            continue

        try:
            # Создаем или перезаписываем файл
            with open(file_name, 'w', encoding='utf-8') as outfile:
                outfile.write(file_content)
            print(f"Успешно восстановлен/обновлен файл: {file_name}")
        except Exception as e:
            print(f"Ошибка при записи файла {file_name}: {e}")

    print("\nПроцесс восстановления завершен.")

if __name__ == "__main__":
    split_merged_txt()
