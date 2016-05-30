//============================================================================
// Name        : ParserOBJto3GL.cpp
// Author      : Štětka Petr (štěty)
// Version     : 0.2
// Copyright   : Private.
// Description : This parser parse OBJ model to 3GL.
//============================================================================

#include <iostream>
#include <fstream>
#include <string>
#include <cstdlib>
#include <string.h>
#include <dirent.h>
#include <algorithm>
#include <sstream>

using namespace std;

typedef struct Model
{
    int vertices;
    int positions;
    int texels;
    int normals;
    int faces;
}
Model;

string findFiles(int *lines)
{
	DIR *dpdf;
	struct dirent *epdf;
	string files;
	string current;

	dpdf = opendir("source");
	if (dpdf != NULL){
	   while (epdf = readdir(dpdf))
	   {
		   current = epdf->d_name;
		   if(current.length() <= 2)
		   {
			   continue;
		   }
		   else
		   {
			   files += epdf->d_name;
			   files += "\n";
			   *lines += 1;
		   }
	   }
	}
	return files;
}

Model getOBJinfo(string fp)
{
    Model model = {0};

    // Open OBJ file
    ifstream inOBJ;
    inOBJ.open(fp.c_str());
    if(!inOBJ.good())
    {
        cout << "ERROR OPENING OBJ FILE" << endl;
        exit(1);
    }

    // Read OBJ file
    while(!inOBJ.eof())
    {
        string line;
        getline(inOBJ, line);
        string type = line.substr(0,2);

        if(type.compare("v ") == 0)
            model.positions++;
        else if(type.compare("vt") == 0)
            model.texels++;
        else if(type.compare("vn") == 0)
            model.normals++;
        else if(type.compare("f ") == 0)
            model.faces++;
    }

    model.vertices = model.faces*3;

    inOBJ.close();
    return model;
}

int countString(const std::string & str, const std::string & obj )
{
    int n = 0;
    string ::size_type pos = 0;
    while((pos = obj.find(str, pos)) != std::string::npos)
    {
    	n++;
    	pos += str.size();
    }
    return n;
}

void extractOBJdata(string fp, float positions[][3], float texels[][2], float normals[][3], int faces[][9])
{
    // Counters
    int p = 0;
    int t = 0;
    int n = 0;
    int f = 0;

    // Open OBJ file
    ifstream inOBJ;
    inOBJ.open(fp.c_str());
    if(!inOBJ.good())
    {
        cout << "ERROR OPENING OBJ FILE" << endl;
        exit(1);
    }

    // Read OBJ file
    while(!inOBJ.eof())
    {
        string line;
        getline(inOBJ, line);
        string type = line.substr(0,2);

        // Positions
        if(type.compare("v ") == 0)
        {
            // Copy line for parsing
            char* l = new char[line.size()+1];
            memcpy(l, line.c_str(), line.size()+1);

            // Extract tokens
            strtok(l, " ");
            for(int i=0; i<3; i++)
                positions[p][i] = atof(strtok(NULL, " "));

            // Wrap up
            delete[] l;
            p++;
        }

        // Texels
        else if(type.compare("vt") == 0)
        {
            char* l = new char[line.size()+1];
            memcpy(l, line.c_str(), line.size()+1);

            strtok(l, " ");
            for(int i=0; i<2; i++)
            {
                texels[t][i] = atof(strtok(NULL, " "));
                if(i == 1)
                	texels[t][i] = -texels[t][i];
            }
            delete[] l;
            t++;
        }

        // Normals
        else if(type.compare("vn") == 0)
        {
            char* l = new char[line.size()+1];
            memcpy(l, line.c_str(), line.size()+1);

            strtok(l, " ");
            for(int i=0; i<3; i++)
                normals[n][i] = atof(strtok(NULL, " "));

            delete[] l;
            n++;
        }

        // Faces
        else if(type.compare("f ") == 0)
        {
            char* l = new char[line.size()+1];
            memcpy(l, line.c_str(), line.size()+1);

            strtok(l, " ");
            for(int i=0; i<9; i++)
            {
            	faces[f][i] = atof(strtok(NULL, " /"));
            }

            delete[] l;
            f++;
        }
    }
    inOBJ.close();
}

void writeH(string fp, string name, Model model)
{
    // Create H file
    ofstream outH;
    outH.open(fp.c_str());
    if(!outH.good())
    {
        cout << "ERROR CREATING H FILE" << endl;
        exit(1);
    }

    // Write to H file
    outH << "// This is a .h file for the model: " << name << endl;
    outH << endl;

    // Write statistics
    outH << "// Positions: " << model.positions << endl;
    outH << "// Texels: " << model.texels << endl;
    outH << "// Normals: " << model.normals << endl;
    outH << "// Faces: " << model.faces << endl;
    outH << "// Vertices: " << model.vertices << endl;
    outH << endl;

    // Write declarations
    outH << "const int " << name << "Vertices;" << endl;
    outH << "const float " << name << "Positions[" << model.vertices*3 << "];" << endl;
    outH << "const float " << name << "Texels[" << model.vertices*2 << "];" << endl;
    outH << "const float " << name << "Normals[" << model.vertices*3 << "];" << endl;
    outH << endl;

    // Close H file
    outH.close();
}

void writeCvertices(string fp, string name, Model model)
{
    // Create C file
    ofstream outC;
    outC.open(fp.c_str());
    if(!outC.good())
    {
        cout << "ERROR CREATING C FILE" << endl;
        exit(1);
    }

    // Write to C file
    outC << "// This is a .c file for the model: " << name << endl;
    outC << endl;

    // Header
    outC << "#include " << "\"" << name << ".h" << "\"" << endl;
    outC << endl;

    // Vertices
    outC << "const int " << name << "Vertices = " << model.vertices << ";" << endl;
    outC << endl;

    outC.close();
}
void writeCpositions(string fp, string name, Model model, int faces[][9], float positions[][3])
{
    // Append C file
    ofstream outC;
    outC.open(fp.c_str(), ios::app);

    // Positions
    outC << "const float " << name << "Positions[" << model.vertices*3 << "] = " << endl;
    outC << "{" << endl;
    for(int i=0; i<model.faces; i++)
    {
        int vA = faces[i][0] - 1;
        int vB = faces[i][3] - 1;
        int vC = faces[i][6] - 1;

        outC << positions[vA][0] << ", " << positions[vA][1] << ", " << positions[vA][2] << ", " << endl;
        outC << positions[vB][0] << ", " << positions[vB][1] << ", " << positions[vB][2] << ", " << endl;
        outC << positions[vC][0] << ", " << positions[vC][1] << ", " << positions[vC][2] << ", " << endl;
    }
    outC << "};" << endl;
    outC << endl;

    // Close C file
    outC.close();
}

void writeCtexels(string fp, string name, Model model, int faces[][9], float texels[][2])
{
    // Append C file
    ofstream outC;
    outC.open(fp.c_str(), ios::app);

    // Texels
    outC << "const float " << name << "Texels[" << model.vertices*2 << "] = " << endl;
    outC << "{" << endl;
    for(int i=0; i<model.faces; i++)
    {
        int vtA = faces[i][1] - 1;
        int vtB = faces[i][4] - 1;
        int vtC = faces[i][7] - 1;

        outC << texels[vtA][0] << ", " << texels[vtA][1] << ", " << endl;
        outC << texels[vtB][0] << ", " << texels[vtB][1] << ", " << endl;
        outC << texels[vtC][0] << ", " << texels[vtC][1] << ", " << endl;
    }
    outC << "};" << endl;
    outC << endl;

    // Close C file
    outC.close();
}

void writeCnormals(string fp, string name, Model model, int faces[][9], float normals[][3])
{
    // Append C file
    ofstream outC;
    outC.open(fp.c_str(), ios::app);

    // Normals
    outC << "const float " << name << "Normals[" << model.vertices*3 << "] = " << endl;
    outC << "{" << endl;
    for(int i=0; i<model.faces; i++)
    {
        int vnA = faces[i][2] - 1;
        int vnB = faces[i][5] - 1;
        int vnC = faces[i][8] - 1;

        outC << normals[vnA][0] << ", " << normals[vnA][1] << ", " << normals[vnA][2] << ", " << endl;
        outC << normals[vnB][0] << ", " << normals[vnB][1] << ", " << normals[vnB][2] << ", " << endl;
        outC << normals[vnC][0] << ", " << normals[vnC][1] << ", " << normals[vnC][2] << ", " << endl;
    }
    outC << "};" << endl;
    outC << endl;

    // Close C file
    outC.close();
}

void write3GLptn(string fp, string name, Model model, int faces[][9], float positions[][3], float normals[][3], float texels[][2])
{
	// Create 3GL file
	ofstream out3GL;
	out3GL.open(fp.c_str());
	if(!out3GL.good())
	{
		cout << "ERROR CREATING 3GL FILE" << endl;
	    exit(1);
	}

	// Write to 3GL file
	out3GL << "//.3gl file for load model: |" << name << "| in OpenGL (Positions, Texels, Normals)." << endl;
	out3GL << "vx" << model.vertices*3 + model.vertices*3 + model.vertices*2 << endl;

	// Positions
	for(int i=0; i<model.faces; i++)
	{
		int vA = faces[i][0] - 1;
	    int vB = faces[i][3] - 1;
	    int vC = faces[i][6] - 1;
	    int vtA = faces[i][1] - 1;
	    int vtB = faces[i][4] - 1;
	    int vtC = faces[i][7] - 1;
	    int vnA = faces[i][2] - 1;
	    int vnB = faces[i][5] - 1;
	    int vnC = faces[i][8] - 1;

	    out3GL << positions[vA][0] << "," << positions[vA][1] << "," << positions[vA][2] << "," << endl;
	    out3GL << texels[vtA][0] << "," << texels[vtA][1] << "," << endl;
	    out3GL << normals[vnA][0] << "," << normals[vnA][1] << "," << normals[vnA][2] << "," << endl;
	    out3GL << positions[vB][0] << "," << positions[vB][1] << "," << positions[vB][2] << "," << endl;
	    out3GL << texels[vtB][0] << "," << texels[vtB][1] << "," << endl;
	    out3GL << normals[vnB][0] << "," << normals[vnB][1] << "," << normals[vnB][2] << "," << endl;
	    out3GL << positions[vC][0] << "," << positions[vC][1] << "," << positions[vC][2] << "," << endl;
	    out3GL << texels[vtC][0] << "," << texels[vtC][1] << "," << endl;
	    out3GL << normals[vnC][0] << "," << normals[vnC][1] << "," << normals[vnC][2] << "," << endl;
	}

	// Close C file
	out3GL.close();
}

void write3GLp(string fp, string name, Model model, int faces[][9], float positions[][3])
{
	// Create 3GL file
	ofstream out3GL;
	out3GL.open(fp.c_str());
	if(!out3GL.good())
	{
		cout << "ERROR CREATING 3GL FILE" << endl;
	    exit(1);
	}

	// Write to 3GL file
	out3GL << "//.3gl file for load model: |" << name << "| in OpenGL (Positions)." << endl;
	out3GL << "vx" << model.vertices*3 << endl;

	// Positions
	for(int i=0; i<model.faces; i++)
	{
		int vA = faces[i][0] - 1;
	    int vB = faces[i][3] - 1;
	    int vC = faces[i][6] - 1;

	    out3GL << positions[vA][0] << "," << positions[vA][1] << "," << positions[vA][2] << "," << endl;
	    out3GL << positions[vB][0] << "," << positions[vB][1] << "," << positions[vB][2] << "," << endl;
	    out3GL << positions[vC][0] << "," << positions[vC][1] << "," << positions[vC][2] << "," << endl;
	}

	// Close C file
	out3GL.close();
}

void write3GLpn(string fp, string name, Model model, int faces[][9], float positions[][3], float normals[][3])
{
	// Create 3GL file
	ofstream out3GL;
	out3GL.open(fp.c_str());
	if(!out3GL.good())
	{
		cout << "ERROR CREATING 3GL FILE" << endl;
	    exit(1);
	}

	// Write to 3GL file
	out3GL << "//.3gl file for load model: |" << name << "| in OpenGL (Positions, Normals)." << endl;
	out3GL << "vx" << model.vertices*3 + model.vertices*3 << endl;

	// Positions
	for(int i=0; i<model.faces; i++)
	{
		int vA = faces[i][0] - 1;
	    int vB = faces[i][3] - 1;
	    int vC = faces[i][6] - 1;
	    int vnA = faces[i][2] - 1;
	    int vnB = faces[i][5] - 1;
	    int vnC = faces[i][8] - 1;

	    out3GL << positions[vA][0] << "," << positions[vA][1] << "," << positions[vA][2] << "," << endl;
	    out3GL << normals[vnA][0] << "," << normals[vnA][1] << "," << normals[vnA][2] << "," << endl;
	    out3GL << positions[vB][0] << "," << positions[vB][1] << "," << positions[vB][2] << "," << endl;
	    out3GL << normals[vnB][0] << "," << normals[vnB][1] << "," << normals[vnB][2] << "," << endl;
	    out3GL << positions[vC][0] << "," << positions[vC][1] << "," << positions[vC][2] << "," << endl;
	    out3GL << normals[vnC][0] << "," << normals[vnC][1] << "," << normals[vnC][2] << "," << endl;
	}

	// Close C file
	out3GL.close();
}
void write3GLpt(string fp, string name, Model model, int faces[][9], float positions[][3], float texels[][2])
{
	// Create 3GL file
	ofstream out3GL;
	out3GL.open(fp.c_str());
	if(!out3GL.good())
	{
		cout << "ERROR CREATING 3GL FILE" << endl;
	    exit(1);
	}

	// Write to 3GL file
	out3GL << "//.3gl file for load model: |" << name << "| in OpenGL (Positions, Texels)." << endl;
	out3GL << "vx" << model.vertices*3 + model.vertices*2 << endl;

	// Positions
	for(int i=0; i<model.faces; i++)
	{
		int vA = faces[i][0] - 1;
	    int vB = faces[i][3] - 1;
	    int vC = faces[i][6] - 1;
	    int vtA = faces[i][1] - 1;
	    int vtB = faces[i][4] - 1;
	    int vtC = faces[i][7] - 1;

	    out3GL << positions[vA][0] << "," << positions[vA][1] << "," << positions[vA][2] << "," << endl;
		out3GL << texels[vtA][0] << "," << texels[vtA][1] << "," << endl;
		out3GL << positions[vB][0] << "," << positions[vB][1] << "," << positions[vB][2] << "," << endl;
		out3GL << texels[vtB][0] << "," << texels[vtB][1] << "," << endl;
		out3GL << positions[vC][0] << "," << positions[vC][1] << "," << positions[vC][2] << "," << endl;
		out3GL << texels[vtC][0] << "," << texels[vtC][1] << "," << endl;
	}

	// Close C file
	out3GL.close();
}

void writeHelp()
{
	cout << "This is help for Parser from OBJ." << endl;
	cout << "1. Program run ./ParserObjTo3GL option" << endl;
	cout << "2. Option: 	1 = 3GL-default 	- .3GL model. for load in OpenGL (Px,Py,Pz, Tx, Ty, Nx,Ny,Nz)." << endl;
	cout << "		2 = 3GL-Position	- .3GL model. for load in OpenGL (Px,Py,Pz)." << endl;
	cout << "		3 = 3GL-PositionNormal  - .3GL model. for load in OpenGL (Px,Py,Pz, Nx,Ny,Nz)." << endl;
	cout << "		4 = 3GL-PositionTexel 	- .3GL model. for load in OpenGL (Px,Py,Pz, Tx, Ty)." << endl;
	cout << "		5 = Files-.c.h		- Create .c and .h files." << endl;
	cout << "		6 = TODO more options	- With .c and .h files." << endl;
	cout << "!!!Author: Petr Štětka (štěty)!!!" << endl;
}

int main(int argc, const char * argv[])
{
	if(strcmp(argv[1], "help") == 0 || strcmp(argv[1], "-h") == 0 || !argv[1])
	{
		writeHelp();
		return 0;
	}

	int option = 0;
	istringstream streamOption(argv[1]);
	streamOption >> option;

	cout << "!!!Hello, this is parser .obj to .3gl!!!" << endl;
	cout << "!!!Author: Petr Štětka (štěty)!!!" << endl << endl;

	int numberFiles = 0;
	int *p_numberFiles;
	p_numberFiles = &numberFiles;
string files = findFiles(p_numberFiles);

	cout << "These files will converted: (" << numberFiles << ")" << endl << files << endl;

	string nameOBJ;
	istringstream filesStream(files);

	while (getline(filesStream, nameOBJ))
	{
		nameOBJ = nameOBJ.substr(0, nameOBJ.size()-4);

		//Files
		string filepathOBJ = "source/" + nameOBJ + ".obj";
		string filepathH = "product/" + nameOBJ + ".h";
		string filepathC = "product/" + nameOBJ + ".c";
		string filepath3GL = "product/" + nameOBJ + ".3gl";

		// Model Info
		Model model = getOBJinfo(filepathOBJ);
		cout << "Model Info" << endl;
		cout << "Positions: " << model.positions << endl;
		cout << "Texels: " << model.texels << endl;
		cout << "Normals: " << model.normals << endl;
		cout << "Faces: " << model.faces << endl;
		cout << "Vertices: " << model.vertices << endl;
		cout << endl;

		// Model Data
		float positions[model.positions][3];    // XYZ
		float texels[model.texels][2];          // UV
		float normals[model.normals][3];        // XYZ
		int faces[model.faces][9];              // PTN PTN PTN

		extractOBJdata(filepathOBJ, positions, texels, normals, faces);
		/*cout << "Model Data" << endl;
		cout << "P1: " << positions[0][0] << "x " << positions[0][1] << "y " << positions[0][2] << "z" << endl;
		cout << "T1: " << texels[0][0] << "u " << texels[0][1] << "v " << endl;
		cout << "N1: " << normals[0][0] << "x " << normals[0][1] << "y " << normals[0][2] << "z" << endl;
		cout << "F1v1: " << faces[0][0] << "p " << faces[0][1] << "t " << faces[0][2] << "n" << endl;*/

		cout << "Option: " << option << endl;
		switch(option)
		{
			case 1:
				write3GLptn(filepath3GL, nameOBJ, model, faces, positions, normals, texels);
				break;
			case 2:
				write3GLp(filepath3GL, nameOBJ, model, faces, positions);
				break;
			case 3:
				write3GLpn(filepath3GL, nameOBJ, model, faces, positions, normals);
				break;
			case 4:
				write3GLpt(filepath3GL, nameOBJ, model, faces, positions, texels);
				break;
			case 5:
				//TODO: edit/fix
				// Write H file
				writeH(filepathH, nameOBJ, model);

				// Write C file
				writeCvertices(filepathC, nameOBJ, model);
				writeCpositions(filepathC, nameOBJ, model, faces, positions);
				writeCtexels(filepathC, nameOBJ, model, faces, texels);
				writeCnormals(filepathC, nameOBJ, model, faces, normals);
				break;
		}
		cout << "File successfully converted." << endl;
		cout << endl;
	}
	cout << "All files successfully converted." << endl;
	return 0;
}
