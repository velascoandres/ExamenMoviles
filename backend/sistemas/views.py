# Create your views here.
# Listar y crear
from rest_framework import generics
from sistemas.serializers import *


class SistemaOperativoAPI(generics.ListCreateAPIView):
    queryset = SistemaOperativo.objects.all()
    serializer_class = SistemaOperativoSerializado


#  Actualizar
class SistemaOperativoUpdate(generics.UpdateAPIView):
    queryset = SistemaOperativo.objects.all()
    lookup_field = 'id'
    serializer_class = SistemaOperativoSerializado


# Borrar
class SistemaOperativoDelete(generics.DestroyAPIView):
    queryset = SistemaOperativo.objects.all()
    lookup_field = 'id'
    serializer_class = SistemaOperativoSerializado
    http_method_names = ["delete"]
    allowed_methods = ('DELETE',)


#  Para la Aplicacion
class AplicacionGetPost(generics.ListCreateAPIView):
    serializer_class = AplicacionSerializada

    # Redefinir el queryset
    def get_queryset(self):
        queryset = Aplicacion.objects.all().order_by('-pk')
        # Parametro de consulta para filtrar Aplicacion por sistema operativo
        sistema_op = self.request.query_params.get('so', None)
        if sistema_op is not None:
            queryset = queryset.filter(sistemaOperativo__id=sistema_op)
        return queryset


#  Actualizar
class AplicacionUpdate(generics.UpdateAPIView):
    queryset = Aplicacion.objects.all()
    lookup_field = 'id'
    serializer_class = AplicacionSerializada


# Borrar
class AplicacionDelete(generics.DestroyAPIView):
    queryset = Aplicacion.objects.all()
    lookup_field = 'id'
    serializer_class = AplicacionSerializada
    http_method_names = ["delete"]
    allowed_methods = ('DELETE',)
