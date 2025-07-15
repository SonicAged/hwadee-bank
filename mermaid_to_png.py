import re
import os
import subprocess
import tempfile
import argparse
from pathlib import Path
import uuid

def extract_mermaid_diagrams(markdown_file):
    """从Markdown文件中提取所有Mermaid图表"""
    with open(markdown_file, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # 匹配Markdown中的Mermaid代码块，包括```mermaid和```格式
    mermaid_pattern = r'```mermaid\s*([\s\S]*?)\s*```'
    diagrams = re.findall(mermaid_pattern, content)
    
    return diagrams

def convert_mermaid_to_png(mermaid_content, output_file, dpi=400):
    """将Mermaid内容转换为PNG图片"""
    # 创建临时文件来存储Mermaid内容
    with tempfile.NamedTemporaryFile(mode='w', suffix='.mmd', delete=False) as temp_file:
        temp_file_name = temp_file.name
        temp_file.write(mermaid_content)
    
    try:
        # 确保输出目录存在
        os.makedirs(os.path.dirname(os.path.abspath(output_file)), exist_ok=True)
        
        # 使用mmdc (Mermaid CLI) 转换Mermaid为PNG
        # -i: 输入文件, -o: 输出文件, -b: 背景色, -p: puppeteer配置
        # 计算适当的宽度以适应高DPI
        scale_factor = dpi / 96  # 标准屏幕DPI是96
        
        # 创建一个包含puppeteer配置的临时JSON文件
        puppeteer_config = {
            "deviceScaleFactor": scale_factor
        }
        
        with tempfile.NamedTemporaryFile(mode='w', suffix='.json', delete=False) as config_file:
            config_file_name = config_file.name
            import json
            json.dump(puppeteer_config, config_file)
        
        cmd = [
            'mmdc',
            '-i', temp_file_name,
            '-o', output_file,
            '-b', 'transparent',
            '-p', config_file_name
        ]
        
        # 执行命令
        result = subprocess.run(cmd, capture_output=True, text=True)
        
        if result.returncode != 0:
            print(f"Error converting diagram: {result.stderr}")
            return False
        
        return True
    
    except Exception as e:
        print(f"Error: {e}")
        return False
    
    finally:
        # 清理临时文件
        if os.path.exists(temp_file_name):
            os.unlink(temp_file_name)
        if os.path.exists(config_file_name):
            os.unlink(config_file_name)

def process_markdown_file(input_file, output_dir=None, dpi=400, replace_in_md=False):
    """处理Markdown文件，提取Mermaid图表并转换为PNG"""
    if output_dir is None:
        # 如果没有指定输出目录，使用输入文件所在目录
        output_dir = os.path.join(os.path.dirname(input_file), 'mermaid_images')
    
    # 确保输出目录存在
    os.makedirs(output_dir, exist_ok=True)
    
    # 获取输入文件名（不含扩展名）
    base_name = os.path.basename(input_file)
    file_name = os.path.splitext(base_name)[0]
    
    # 提取Mermaid图表
    diagrams = extract_mermaid_diagrams(input_file)
    
    if not diagrams:
        print(f"No Mermaid diagrams found in {input_file}")
        return
    
    print(f"Found {len(diagrams)} Mermaid diagrams in {input_file}")
    
    # 转换图表
    converted_files = []
    for i, diagram in enumerate(diagrams):
        # 创建唯一的文件名
        diagram_id = str(uuid.uuid4())[:8]
        output_file = os.path.join(output_dir, f"{file_name}_diagram_{i+1}_{diagram_id}.png")
        
        print(f"Converting diagram {i+1}/{len(diagrams)}...")
        if convert_mermaid_to_png(diagram, output_file, dpi):
            print(f"Successfully converted to {output_file}")
            converted_files.append((diagram, output_file))
        else:
            print(f"Failed to convert diagram {i+1}")
    
    # 如果需要，替换原Markdown文件中的Mermaid代码块
    if replace_in_md and converted_files:
        replace_mermaid_with_images(input_file, converted_files)

def replace_mermaid_with_images(markdown_file, converted_files):
    """在Markdown文件中替换Mermaid代码块为图片引用"""
    with open(markdown_file, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # 对于每个已转换的图表，替换原Mermaid代码块为图片引用
    for mermaid_content, image_path in converted_files:
        # 使用相对路径
        rel_path = os.path.relpath(image_path, os.path.dirname(markdown_file))
        # 转换Windows路径分隔符为正斜杠
        rel_path = rel_path.replace('\\', '/')
        
        # 创建替换模式，需要处理多行和转义特殊字符
        # 我们使用原始的mermaid内容来确保匹配正确的块
        escape_content = re.escape(mermaid_content)
        pattern = f"```mermaid\\s*{escape_content}\\s*```"
        
        # 替换为Markdown图片引用
        replacement = f"![Mermaid Diagram]({rel_path})"
        content = re.sub(pattern, replacement, content)
    
    # 创建带有后缀的新文件名
    base_name = os.path.basename(markdown_file)
    file_name, ext = os.path.splitext(base_name)
    new_file = os.path.join(os.path.dirname(markdown_file), f"{file_name}_with_images{ext}")
    
    # 写入新文件
    with open(new_file, 'w', encoding='utf-8') as f:
        f.write(content)
    
    print(f"Created new Markdown file with image references: {new_file}")

def main():
    parser = argparse.ArgumentParser(description='Convert Mermaid diagrams in Markdown to PNG images')
    parser.add_argument('input_file', help='Input Markdown file path')
    parser.add_argument('--output-dir', '-o', help='Output directory for PNG images')
    parser.add_argument('--dpi', '-d', type=int, default=400, help='DPI for output images (default: 400)')
    parser.add_argument('--replace', '-r', action='store_true', help='Replace Mermaid code with image references in a new Markdown file')
    
    args = parser.parse_args()
    
    process_markdown_file(args.input_file, args.output_dir, args.dpi, args.replace)

if __name__ == "__main__":
    main()